var http_request = require('request');var logger = require('log4js').getLogger("normal");/* 调用API接口的请求工具类 */var ApiHttpUtils = {    /**     * @param ops 必填     * {     *     url: 接口地址  可出传 相对地址 和 绝对地址     *     data: 接口的请求参数     *     req: 用户的请求对象信息     *     headers: 用户的请求头信息     * }     * @param dataType 返回数据类型 可选参数 默认json     * @param callback 回调函数，可选     * */    get: function (ops, dataType, callback) {        if (!callback) {            callback = dataType;            dataType = "json";        }        dataType || (dataType = "json");        dataType = dataType.toLowerCase();        if (typeof ops == "string") {            ops = {url: ops};        }        if (ops.url.indexOf("http") != 0) {            if (ops.req) {                //API域名的域名跟用户请求的地址一致                //如用户请求地址为http://www.lord.com/，对应的API域名也为http://www.lord.com/                ops.url = ops.req.protocol + "://" + ops.req.headers.host + ops.url;            }        }        var params = [];        //拼接GET请求完整的url        //get方法是把参数转换为 param1=value1&param2=value2格式        if (ops.data) {            for (var key in ops.data) {                if (typeof ops.data[key] != "undefined" && ops.data[key] != null) {                    params.push(key + "=" + encodeURIComponent(ops.data[key]));                }            }            if (ops.url.indexOf("?") > 0) {                ops.url += "&";            } else if(params.length > 0) {                ops.url += "?";            }            ops.url += params.join("&");            delete params;        }        //将客户端的请求头信息都传到API端        var headers = {            Referer: ops.req.headers.referer        };        if (ops.headers) {            for (var key in ops.headers) {                headers[key] = ops.headers[key];            }        }        //如果有带 req 把cookie带上，将node.js客户端的Cookies信息传输到API端        if (ops.req && ops.req.headers.cookie) {            headers.Cookie = ops.req.headers.cookie;        }        var methodMsg = "ApiHttpUtils.js.get() 访问接口: " + ops.url;//调用的方法信息        logger.debug(methodMsg + " dataType:" + dataType);        ops.dataType = dataType;        http_request.get({url: ops.url, headers: headers}, function (error, response, body) {            var err = "";            if (!response) {                logger.error(methodMsg + " 找不到该链接。。。");                err = ops.url + " 找不到该链接。。。";            } else if (response.statusCode != 200) {                logger.error(response.body);                logger.error(methodMsg + " 响应错误statusCode=" + response.statusCode);                if (dataType == "json") {                    body = {};                }            } else {                if (dataType == "json" && !(ops.headers && ops.headers["Http-X-Pjax"]) && body) {                    try {                        body = JSON.parse(body);                    } catch (e) {                        logger.error(methodMsg + " 返回结果不是JSON格式body=" + body + "\n", e);                        err = e;                    }                }            }            callback && callback(err, body);        });    },    post: function (ops, dataType, callback) {        if (!callback) {            callback = dataType;            dataType = "json";        }        dataType || (dataType = "json");        dataType = dataType.toLowerCase();        if (typeof ops == "string") {            ops = {url: ops};        }        if (ops.url.indexOf("http") != 0) {            if (ops.req) {                ops.url = ops.req.protocol + "://" + ops.req.headers.host + ops.url;            }        }        if (ops.data) {            for (var key in ops.data) {                if (typeof ops.data[key] == "undefined" || ops.data[key] == null) {                    delete ops.data[key];                }            }        }        //将客户端的请求头信息都传到API端        var headers = {            Cookie: ops.req.headers.cookie,            Referer: ops.req.headers.referer        };        if (ops.headers) {            for (var key in ops.headers) {                headers[key] = ops.headers[key];            }        }        var body;        var form;        if (headers['Content-Type'] == "application/json") {            body = JSON.stringify(ops.data);            headers['Content-Length'] = ops.data.length;        } else {            form = ops.data;        }        //post 方式的参数 用 form 参数去传        http_request.post({url: ops.url, headers: headers, body: body, form: form}, function (error, response, body) {            var err = "";            var methodMsg = "ApiHttpUtils.js.post() 访问接口: " + ops.url;//调用的方法信息            if (!response || typeof response == "undefined") {                logger.error(methodMsg + " 找不到该链接。。。");                err = ops.url + " 找不到该链接。。。";            } else if (response.statusCode != 200) {                logger.error(response.body);                logger.error(methodMsg + " 响应错误statusCode:" + response.statusCode);                if (dataType == "json") {                    body = {};                }            } else {                if (dataType == "json" && body) {                    try {                        body = JSON.parse(body);                    } catch (e) {                        logger.error(methodMsg + " 返回结果不是JSON格式body=" + body + "\n", e);                        err = e;                    }                }            }            callback && callback(err, body);        });    }};module.exports = ApiHttpUtils;