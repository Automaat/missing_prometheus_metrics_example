db.createUser({
    "user": "order_local",
    "pwd": "order_local",
    "roles": [
        {
            "role": "readWrite",
            "db": "order_local_db"
        }
    ]
});
