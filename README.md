# Canter Product API Server

### Usage

To run in development mode, use:
```shell
sbt run
```

### API endpoints

```shell
/products           GET         // Get all products.
/products/{id}      GET         // Get a single product by id.
/products           POST        // Create a new product.
/products/{id}      PUT         // Update a product by id.
/products/{id}      DELETE      // Delete a single product by id.
```
