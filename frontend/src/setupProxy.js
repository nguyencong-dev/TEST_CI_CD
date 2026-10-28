const { createProxyMiddleware } = require("http-proxy-middleware");

const backendHost = process.env.BACKEND_HOST || "backend";
const backendPort = process.env.BACKEND_PORT || "8080";

module.exports = function(app) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: process.env.BACKEND_URL || `http://${backendHost}:${backendPort}`,
      pathRewrite: { "^/api": "" },
      changeOrigin: true
    })
  );
};
