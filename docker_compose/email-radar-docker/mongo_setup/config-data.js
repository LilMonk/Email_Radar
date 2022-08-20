db = db.getSiblingDB("email_spam_detection");
// db.createCollection("source");
db.createCollection("emails");

admin = db.getSiblingDB("admin");
admin.createUser(
 {
    user: "rootadmin",
    pwd: "rootadmin",
    roles: [ { role: "root", db: "admin" } ]
 });
 db.getSiblingDB("admin").auth("rootadmin", "rootadmin");
 rs.status();
