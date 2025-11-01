@echo off
REM =====================================================
REM Execute all SQL files inside PostgreSQL Docker container (AI Module)
REM =====================================================

REM Change to your SQL folder
cd /d D:\ECommerence_fullstack\sql\aimodule

REM Execute SQL files inside the aimodule container with progress messages

echo =====================================================
echo Running: 05_foreign_customers_vendors.sql
docker exec -i postgres-aimodule psql -U admin -d aimodule_db < 05_foreign_customers_vendors.sql

echo =====================================================
echo Running: 10_chat_session.sql
docker exec -i postgres-aimodule psql -U admin -d aimodule_db < 10_chat_session.sql

echo =====================================================
echo Running: 15_chat_history.sql
docker exec -i postgres-aimodule psql -U admin -d aimodule_db < 15_chat_history.sql

echo =====================================================
echo (Optional) Running: sample_query.sql
docker exec -i postgres-aimodule psql -U admin -d aimodule_db < sample_query.sql

echo =====================================================
echo All AI Module SQL files executed successfully inside Docker!
echo =====================================================
pause
