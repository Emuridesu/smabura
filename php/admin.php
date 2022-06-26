<?php

$message='登録するＩＤパスワードを入力してください。';

if ($_SERVER["REQUEST_METHOD"] == 'POST') {
    $id = $_POST['id'];
    $pass = $_POST['pass'];

    $f = @fopen('id_and_password.csv', 'a');
    fputcsv($f,[$id, $pass]);
    fclose($f);
    $message='ID=' . $id . ' を登録しました ';
}
?>
<!doctype html>
<html lang="ja">
<head>
<title>index</title>
<meta charset="UTF-8"/>
<style>

    </style>
</head>
<body>
    <h1>Admin</h1>
    <p><?= $message ?></p>
    <table>
    <form method="post" action="./admin.html">
    <tr>
    <th><label>id:</label></th>
    <td><input type="text" name="id"></td>
    </tr>
    <tr>
    <th><label>pass:</label></th>
    <td><input type="password" name="pass"></td>
    </tr> 
    <tr>
    <th></th>
    <td><input type="submit" value="登録"></td>
    </tr>
    <p><a href="login.html">ログインページ</a></p>
    </form>
    </table>
</body>
</html>