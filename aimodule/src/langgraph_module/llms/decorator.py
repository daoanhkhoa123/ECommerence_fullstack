from functools import lru_cache


def llm_factory(cls):

    @lru_cache(maxsize=8, typed=True)
    def initialize(*args, **kwargs):
        return cls(*args, **kwargs)
    
    return initialize