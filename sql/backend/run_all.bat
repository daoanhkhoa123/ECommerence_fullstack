@echo off
REM =====================================================
REM Execute all SQL files inside PostgreSQL Docker container
REM =====================================================

REM Change to your SQL folder
cd /d backend

REM Execute SQL files inside the container with progress messages

echo =====================================================
echo Running: 05_base_entity.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 05_base_entity.sql

echo =====================================================
echo Running: 10_trigger_append_base_ent.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 10_trigger_append_base_ent.sql

echo =====================================================
echo Running: 15_accounts.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 15_accounts.sql

echo =====================================================
echo Running: 20_customers.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 20_customers.sql

echo =====================================================
echo Running: 25_vendors.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 25_vendors.sql

echo =====================================================
echo Running: 30_trigger_check_role.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 30_trigger_check_role.sql

echo =====================================================
echo Running: 33_categories.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 33_categories.sql

echo =====================================================
echo Running: 35_products.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 35_products.sql

echo =====================================================
echo Running: 40_vendors_products.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 40_vendors_products.sql

echo =====================================================
echo Running: 45_trigger_check_vendor_product.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 45_trigger_check_vendor_product.sql

echo =====================================================
echo Running: 50_orders.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 50_orders.sql

echo =====================================================
echo Running: 55_orders_items.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 55_orders_items.sql

echo =====================================================
echo Running: 60_payments.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 60_payments.sql

echo =====================================================
echo Running: 99_sample_datas.sql
docker exec -i postgres-backend psql -U admin -d backend_db < 99_sample_datas.sql

echo =====================================================
echo All SQL files executed inside Docker successfully!
echo =====================================================
pause
