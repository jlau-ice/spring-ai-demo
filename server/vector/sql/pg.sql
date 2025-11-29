--查看是否有 'vector' 拓展
SELECT name, default_version, installed_version
FROM pg_available_extensions
WHERE name = 'vector';

CREATE SCHEMA IF NOT EXISTS embedding_vector;
-- 当前数据库中创建 vector 拓展。vector 扩展（即 PgVector）提供了 vector 数据类型和相关的索引（如 HNSW、IVFFlat）操作符，是 PostgreSQL 能够存储和高效检索向量数据的核心。
CREATE
EXTENSION IF NOT EXISTS vector;
-- 创建 hstore 扩展。 hstore 扩展提供了用于存储键值对数据的 hstore 数据类型。在 PgVector 存储中，它常被用来存储向量关联的 Metadata（元数据），例如文档来源、作者、时间戳等信息。
CREATE
EXTENSION IF NOT EXISTS hstore;
-- 创建 uuid-ossp 扩展。 该扩展提供了生成 UUID (Universally Unique Identifier) 的函数，例如 uuid_generate_v4()。这在创建表时用作主键，保证每条记录 ID 的唯一性。
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";
-- 查看名为 'vector' 的扩展是否已经被安装到当前数据库中
SELECT extname, extversion, extnamespace::regnamespace AS schema
FROM pg_extension
WHERE extname = 'vector';
-- 创建一个名为 vector_store 的表，用于存储您的向量数据。
CREATE TABLE IF NOT EXISTS vector_store
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content   text,
    metadata  json,
    embedding vector(1536)
    );
-- 修改 vector_store 表中 embedding 列的定义。将向量列的期望维度从 1536 修改为 1024。这条语句是用来解决您上一个错误 "expected 1536 dimensions, not 1024" 的核心操作。它确保数据库的表结构与您的 Embedding Model (Ollama/DashScope) 实际生成的 1024 维向量相匹配。
ALTER TABLE vector_store
ALTER COLUMN embedding TYPE vector(1024);

select *
from vector_store