<?php
include "connect.php";
$query = "SELECT * FROM productnew ORDER BY id DESC";
// $page = $_POST['page'];
// $total = 5;// can lay 5 sp tren 1 trang
// $pos = ($page-1)*total; // 0,5         5,5

// $query = "SELECT * FROM productnew LIMIT '.$pos.', '.$total.'";
$data = mysqli_query($conn, $query);
$result = array(); 
while ($row = mysqli_fetch_assoc($data)) {
    $result[] = ($row);
    //code..
}

// print_r ($result);

if (!empty($result)){
    $arr = [
        'success' => true,
        'message' => "thanh cong",
        'result' => $result
    ];

} else {
    $arr = [
        'success' => false,
        'message' => "khong thanh cong",
        'result' => $result
    ];

}

print_r(json_encode($arr));


?>