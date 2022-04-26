## Get image list
```
GET /images
    RETURN {
        id:long,
        name:string,
        type:org.springframework.http.MediaType,
        size:string
    }
```

## Post image
```
POST /images
    RETURN 201
  | RETURN 415 -> Unsupported media type
```

## Get image with id
```
GET /images/{id}
    RETURN 201
  | RETURN 404 -> image not found
```

## Delete image
```
DELETE /images/{id}
    RETURN 201
  | RETURN 404 -> image not found
```

## Apply algorithm
```
GET /images/{id}?algorithm=X&p1=Y&p2=z
    RETURN 200
  | RETURN 400 -> no algo, no params, no value
  | RETURN 404 -> image not found
  | RETURN 500 -> Internal problem
```