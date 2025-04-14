const { generateService } = require("@umijs/openapi");

generateService({
  requestLibPath: "import request from '@/request'",
  schemaPath: "http://110.41.130.6:8101/api/v2/api-docs",
  serversPath: "./src",
});
