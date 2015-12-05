var mysql = require('mysql');
var Response = require('./Response');
var Const = require('./Const');

var connection = mysql.createConnection({
    host    : 'localhost',
    user    : 'root',
    password: ''
});
connection.connect();
connection.query('USE stackx');


var answer = {};

answer.vote = function(req, callback) {
    var sql = 'SELECT answer_id, user_id, value FROM vote_answer WHERE answer_id=? AND user_id=?';
    connection.query(sql, [req.answerId, req.userId], function(err, results) {
        if (results.length === 0) {
            var sql = 'INSERT INTO vote_answer (answer_id, user_id, value) VALUES (?, ?, ?)';

            connection.query(sql, [req.answerId, req.userId, req.value], function(err, results) {
                var resp;
                if (err) {
                    resp = Response(err.errno, err.message, {});
                } else {
                    resp = Response(Const.STATUS_OK, '', results);
                }
                callback(resp);
            });
        } else {
            if (results[0].value !== req.value) {
                var sql = 'UPDATE vote_answer SET value=? WHERE answer_id=? AND user_id=?';
                connection.query(sql, [req.value, req.answerId, req.userId], function(err, results) {
                    var resp;
                    if (err) {
                        resp = Response(err.errno, err.message, {});
                    } else {
                        resp = Response(Const.STATUS_OK, '', results);
                    }
                    callback(resp);
                });
            } else {
                callback(Response(1, 'You can only vote once.', {}));
            }
        }
    })
}

module.exports = answer;

/*connection.connect();

connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields) {
  if (err) throw err;
  console.log('The solution is: ', rows[0].solution);
});

connection.end();*/