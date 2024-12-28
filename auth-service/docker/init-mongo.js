db = db.getSiblingDB('ensat_carrer_app');

// Create collections
db.createCollection("loginInfo");
db.createCollection("sign_up_requests");
db.createCollection("partners");
db.createCollection("alumni");
db.createCollection("student");

// Insert sample data
db.loginInfo.insert({
    email: "admin@example.com",
    password: "hashedPassword",
    role: "admin",
    createdAt: new Date(),
    updatedAt: new Date()
});
