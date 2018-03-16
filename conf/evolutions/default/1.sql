# --- !Ups
CREATE TABLE products (
  id        SERIAL        PRIMARY KEY,
  name      VARCHAR(255)  NOT NULL,
  category  VARCHAR(255)  NOT NULL,
  price     VARCHAR(8)    NOT NULL
);

CREATE TABLE product_details(
  id        SERIAL        PRIMARY KEY,
  key       VARCHAR(255)  NOT NULL,
  value     VARCHAR(255)  NOT NULL,
  product_id BIGINT       NOT NULL REFERENCES products (id)
);

# --- !Downs
DROP TABLE product_details;
DROP TABLE products;
