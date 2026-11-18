-- Database per Service: um banco e um usuário para cada microsserviço.
-- (Em produção, cada banco poderia ficar em uma instância separada.)
CREATE USER identidade WITH PASSWORD 'identidade';           CREATE DATABASE identidade_db OWNER identidade;
CREATE USER producao WITH PASSWORD 'producao';               CREATE DATABASE producao_db OWNER producao;
CREATE USER qualidade WITH PASSWORD 'qualidade';             CREATE DATABASE qualidade_db OWNER qualidade;
CREATE USER estoque WITH PASSWORD 'estoque';                 CREATE DATABASE estoque_db OWNER estoque;
CREATE USER pedidos WITH PASSWORD 'pedidos';                 CREATE DATABASE pedidos_db OWNER pedidos;
CREATE USER rastreabilidade WITH PASSWORD 'rastreabilidade'; CREATE DATABASE rastreabilidade_db OWNER rastreabilidade;
CREATE USER notificacoes WITH PASSWORD 'notificacoes';       CREATE DATABASE notificacoes_db OWNER notificacoes;
