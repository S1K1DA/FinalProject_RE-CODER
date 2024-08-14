//
// // document.addEventListener('DOMContentLoaded', function() {
//
// // 지도를 표시할 div 요소를 선택
//     var container = document.getElementById('matching-map');
//     var options = {
//         center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도 초기 중심 좌표
//         level: 3 // 지도 확대 레벨
//     };
//
// // 지도 생성
//     var map = new kakao.maps.Map(container, options);
//
//
//     $.ajax({
//         type: "POST",
//         url: "/matching-area/location",
//         data: {basicUserNo: 1},
//         success: function (res) {
//             console.log(res);
//             const userAddr = res;
//             initializeMap(userAddr);
//         },
//         error: function (err) {
//             console.log(err);
//         }
//     });
//
//     var geocoder = new kakao.maps.services.Geocoder();
//
// // 주소에 따른 위치로 지도를 이동시키는 함수
//     function initializeMap(address) {
//
//
//         // 주소로 좌표를 검색합니다
//         geocoder.addressSearch(address, function (result, status) {
//
//             // 정상적으로 검색이 완료됐으면
//             if (status === kakao.maps.services.Status.OK) {
//
//                 var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
//
//                 // 결과값으로 받은 위치를 마커로 표시합니다
//                 var marker = new kakao.maps.Marker({
//                     map: map,
//                     position: coords
//                 });
//
//                 // 인포윈도우로 장소에 대한 설명을 표시합니다
//                 var infowindow = new kakao.maps.InfoWindow({
//                     content: '<div style="width:150px;text-align:center;padding:6px 0;">내 위치</div>'
//                 });
//                 infowindow.open(map, marker);
//
//                 // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
//                 map.setCenter(coords);
//             }
//         });
//     }
// // });

var mapContainer = document.getElementById('matching-map'); // 지도를 표시할 div
mapOption = {
    center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
};

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

// 주소로 좌표를 검색합니다
geocoder.addressSearch('경기 안양시 만안구 안양로 303', function(result, status) {

    // 정상적으로 검색이 완료됐으면
    if (status === kakao.maps.services.Status.OK) {

        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;">우리회사</div>'
        });
        infowindow.open(map, marker);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
    }
});