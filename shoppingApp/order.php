<?php
include "connect.php";

// Kiểm tra và hiển thị nội dung của php://input
// echo "php://input: ";
// echo file_get_contents('php://input');

// echo "\n\n";

// // Kiểm tra và hiển thị các biến POST
// echo "POST data: ";
// print_r($_POST);

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $phonenumber = isset($_POST['phonenumber']) ? $_POST['phonenumber'] : '';
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    $totalprice = isset($_POST['totalprice']) ? $_POST['totalprice'] : '';
    $iduser = isset($_POST['iduser']) ? $_POST['iduser'] : '';
    $address = isset($_POST['address']) ? $_POST['address'] : '';
    $amount = isset($_POST['amount']) ? $_POST['amount'] : '';
    $detail = isset($_POST['detail']) ? $_POST['detail'] : '';

    if ($phonenumber && $email && $totalprice && $iduser && $address && $amount) {
        $query = "INSERT INTO orders(`iduser`, `address`, `phonenumber`, `email`, `amount`, `totalprice`) 
                  VALUES ('$iduser', '$address', '$phonenumber', '$email', '$amount', '$totalprice')";
        // echo $query;
        $data = mysqli_query($conn, $query);

        if($data == true){
            $query = "SELECT id AS iddonhang FROM orders WHERE iduser = '$iduser' ORDER BY id DESC LIMIT 1";
            $data = mysqli_query($conn, $query);
            $result = null;
            while ($row = mysqli_fetch_assoc($data)) {
                $result = $row; 
            }

            if (!empty($result)){
                $idorder = $result['iddonhang'];
                $details = json_decode($detail, true);
                foreach ($details as $value) {
                    $idP = $value['idP'];
                    $amount = $value['amount'];
                    $priceProduct = $value['priceProduct'];
                    $truyvan = "INSERT INTO orderdetail(`idorder`, `idproduct`, `amount`, `price`) 
                                VALUES ('$idorder', '$idP', '$amount', '$priceProduct')";
                    // echo $truyvan;
                    $data = mysqli_query($conn, $truyvan);
                    if($data == true){
                        $arr = [
                            'success' => true,
                            'message' => "thanh cong"
                        ];
                    } else {
                        $arr = [
                            'success' => false,
                            'message' => "khong thanh cong"
                        ];
                    }
                }
            } else {
                $arr = [
                    'success' => false,
                    'message' => "khong thanh cong",
                    'result' => $result
                ];
            }
            print_r(json_encode($arr));
        } else {
            $arr = [
                'success' => false,
                'message' => "fail"
            ];
            print_r(json_encode($arr));
        }
    } else {
        echo "Thiếu tham số đầu vào. Các tham số nhận được: ";
        print_r($_POST);
    }
} else {
    echo "Yêu cầu không hợp lệ";
}
?>
