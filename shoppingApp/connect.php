<?php
$host = "localhost";
$user = "root";
$pass = "";
$database = "datashopping";

$conn = mysqli_connect($host, $user, $pass, $database);
mysqli_set_charset($conn, "utf8");

if($conn){
    //code...
    // echo "ket noi thanh cong";
}


?>