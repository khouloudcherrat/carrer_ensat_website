db = db.getSiblingDB('ensat_carrer_app');

// Create collections
db.createCollection("loginInfo");
db.createCollection("sign_up_requests");
db.createCollection("partners");
db.createCollection("alumni");
db.createCollection("student");

// Insert sample data
db.loginInfo.insertMany([
    {
      email: "admin@example.com",
      password: "$2a$12$AwRfjEJaXyEm7rf1k.Iee.O0.HO617NNRnrFS75jnfW6OaFL4kzt2", // "adminpass"
      role: "admin",
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      email: "student1@example.com",
      password: "$2a$12$hDkPC1Lhwqoh7sW7N00PsOSGvf/hY1Vheq0tms.oVizHhk2XzAvn6", // "studentpass"
      role: "student",
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      email: "alumni1@example.com",
      password: "$2a$12$feVgkSM0Sy.HKdu.4hhX2e78vSQYS4/icDnbCNttf8lRMoMkyDPpq", // "alumnipass"
      role: "alumni",
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ]);
