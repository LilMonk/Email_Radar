rsconf = {
  _id: "rs0",
  members: [{ _id: 0, host: "mongo1:27017" }],
};

rs.initiate(rsconf);
rs.status();

// wait for node to become primary.
//while (! db.isMaster().ismaster ) { sleep(1000) }
