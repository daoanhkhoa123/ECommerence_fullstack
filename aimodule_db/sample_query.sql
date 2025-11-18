SELECT column_name, column_default, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'accounts' AND column_name IN ('id', 'account_id');