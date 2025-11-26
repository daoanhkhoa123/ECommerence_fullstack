# General use cases

## Backend 
Typical use cases with simple create-read-update-delete data manipulation in database. There are 2 user's role essentially: customer and vendor. Vendor post products with informations about categories, price and description. Customer can order and pay them.

## Aimodule
Recommendation, search engines by llm langgraph. Use normal and vector database. Machine learning trained, or deep learning model finetuned on the data to provide predictive statistical information. There are 3 main functionalities, allocation, langgraph_module, machine learning/ deep learning used for analysis and predict statistics.

Allocation is used for synchronizing subset data between dackend database and aimodule database, it is not required to have the exact replica, and should not be, it should only contain informations that are necessary for the recommendation and analysis purpose, such as product informations, or vendor's information are copied. Furthermore, aggregated datas are collected in here.

Langgraph module are a langgraph graph that can fetch data and compromise, analysis it and answer to user's prompt as a recommendation engines.

For predictive machine learning/ deep learning module, aggregate data can beused to trained, as an input to produce number. Or to prodive an overview of statistic numbers and analysis for revenue, for adminstrations and vendors.

## Databases
There are 2 main databases: one for backend and one for aimodule, with an addition of vector databse. Backend database handles traidional data operations while aimodule database is heavy on reading operations, not writing because of training requirements only need to fetching datas, not creating. 

## Kafka
Used to synchronize between datas the two module. Aimodule and backend do not use the same container because unlike normal operation, llms are very gpu-heavy, with massive reading throughput while little-to-none writing demands.
Backend usually prefer more cpu, and a more balanced reading-writing performance database with redundancy. This is also used to send, and recieve messages of the chat messages between llms and users, (backend and aimodule). 

# Technical Stacks

## Backend

- Java 24 
- Springboot Maven project
- Jwt authencation
- JPA repository

## Aimodule

- Python 3.12
- Llms called from apis
- Hugging face for local llms
- Sklearn for machine learning, Pytorch for deep learning
- SqlAlchemy for database operation
- ConfluentKafka for kafka operation
- Pydantic setting for reading configurations

## Databases

- Postgre database, postgre vectordatabase

## Kafka

- Apache Kafka, KRaft mode

## Deployment

- All should be containerized (docker)

# System Architecture

## Backend

Typical layers-by-layers architecture:

- Entity: Domain-defined entities and database models
- Repository: JPA database interface
- Services: Very specific semantic, meaningful use cases, events. Each service functionality performs sets of repository (data) operations, or provides grouped data that is meaningful to the domain, and can be categorized as one events. Also, exceptions are thrown here.
- Application: Orchestration of the specific use with cases. Currently, this has the same cardinality of functionalities as the controller. This is where events are produced, current session's information is used.
- Controller: Apis, entrypoints of the software, this is also where the authencations happend.
- Dto: Data that is used by controllers to commincates. This is not events dtos, which are defined in kafka package.

Aside from those, there are support layers:
- Exception: Defines exception that are specified for each backend exceptions, and a global exception handler to catch them to throw http status errros instead.
- Config: Generic config for the whole applications, such as swagger custom api configuration
- Security: Security functinalities are defined here, such as Jwt filter, security configuration.
- Enums: Data-type typing for entities
- Kafka: Main kafka functionalities are consumer and the producer, dto for event dtos. Topics are defined in enums in groups of domain-entity for easier finding.

## Aimodule

Vectored layer architecture: "allocation" for synchronizing data between backend and aimodule, "langgraph_module" for langgraph operation, furthermore ai functionalities such as "ml_module", "dl_module" should be defined the same way. Since every major vectored functionalities operate commonly on database (domain entities, repository interface, model, repository implementation, unit-of-work interface, unit-of-work implementation), there are some similarities betweent these.

* Allocation

Domain: contain domain entity and repository interface.
Adapters: Persistence contains database config, unit of work (optional), models and repositories implementation

Kafka: defined in adapters/message_bus/broker, kafka consumer and producer is defined here, consumer is functions that help create seperate thread (so that it's always listening), while producer is a class that will be put into app.state to be called manually, and be shared along the app so that it does not get replicated:

```python
# aimodule\src\allocation\entrypoints\startup.py

consumer = create_kafka_consumer_with_retries(...)
if consumer:
    asyncio.create_task(consume_forever(consumer))
...
app.state.kafka_producer = KafkaProducer()
```

Also, we use a topic listener handling by decoration that is used like this

```python
# aimodule\src\allocation\entrypoints\message_broker\consumer\consumer_handlers\category_handler.py
@register_topic("category.read.delete")
async def handle_category_delete(event: dict):
    ...
```


Which required to be loaded and registered at the start up
```python
# aimodule\src\allocation\entrypoints\startup.py
load_all_handlers()
logger.info(f"[Kafka] Topics registered: {list_registered_topics()}")
```

Services: Handle use cases, orchestration of domain logic, perform database operation only. This is simple because it only writes into database for synchronization, which does not need any further processing (producing events, authentication).

Entrypoints: Only message broker are mostly used to handle consumer, or producer. Schemas are defined in this folder for formalizing recieving and sending datas, if it send back and forth to nother service, the schemas on both side has to be the same.

* Langgraph module

The only additional domain entity and model is the chat message, which is unique to this module, so i defined it in here, in the same color as the allocation module. 

Serivce in this module is responsible for calling the graph, save the chat messages and return the result back.

For the langgraph, i decided to do it in layers-by-layers architectural way:

LLms: Contains the llm interface, llm and embedding models, each is a class.

State: folder contains graph states, there are 3 main states: input states, output states and hidden (state schema) states for passing variable in graph's computations.

Node: for defining nodes for the graph. Each node is a function, and can be injected in run time (repository, llms) defined in ContextSchema and are used like this:

``` python
# aimodule\src\langgraph_module\nodes\prep_node.py

@inject_context
def intent_classifier(state: PrepState, context:ContextSchema) -> PrepState:
    ...
```
Graph: Each graph should be defined in one file, and is a function to build a graph, then call itself for initilizing to avoid replicas:

```python
# aimodule\src\langgraph_module\graphs\basic_chat_graph.py
def build_chat_graph() -> CompiledStateGraph:
    graph = StateGraph(state_schema=PrepState, input_schema=InputState, output_schema=OutputState)
    ...
    return graph.compile()
graph = build_chat_graph() 
```

Exception: Contains sepcific exceptions produces by langgraph, if any

Entrypoints: same as above.

* Configs

They includes logging, settings (database url, kafka url), and keys (api, which can not be seen). Secrets apis re defined in .key while normal application configurations are defined iin .env

``` python
# aimodule\src\configs\settings.py
_ENV_FILE = Path(__file__).parent.parent / ".env"
_ENV_FILE_ENCODING = "utf-8"
```

* Main Application

Since before starting application, we need to set up the loggings, register kafka consumers' topic, loading models for database, etc. We do the setups in main.py and actually start up the application at startup.py

``` python
# aimodule\src\main.py
if __name__ == "__main__":
    uvicorn.run("src.startup:app",
            host="127.0.0.1",
            port=8000,
            reload=setting.debug)
```

And this is how we run the application

``` cmd
@echo off
python -m src.main
pause
```
