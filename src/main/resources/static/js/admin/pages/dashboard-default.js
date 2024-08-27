document.addEventListener('DOMContentLoaded', function () {
    setTimeout(function () {
        floatchart();
    }, 500);
});

function floatchart() {
    // 주간 매출 차트 설정
    var weeklyOptions = {
        chart: {
            type: 'line',
            height: 300,
            sparkline: {
                enabled: false
            },
            toolbar: {
                show: false
            }
        },
        dataLabels: {
            enabled: false
        },
        colors: ['#2196f3', '#ff5722'], // Adjust colors for your needs
        stroke: {
            curve: 'smooth',
            width: 3
        },
        series: [
            {
                name: '결제 완료',
                data: [120, 150, 80, 200, 180, 220, 150] // Example data
            },
            {
                name: '취소',
                data: [20, 30, 15, 40, 30, 25, 35] // Example data
            }
        ],
        xaxis: {
            type: 'category',
            categories: ['1주차', '2주차', '3주차', '4주차', '5주차', '6주차', '7주차'] // Example categories
        },
        grid: {
            strokeDashArray: 4
        },
        tooltip: {
            theme: 'dark'
        }
    };

    // 월간 매출 차트 설정
    var monthlyOptions = {
        chart: {
            type: 'bar',
            height: 480,
            stacked: true,
            toolbar: {
                show: false
            }
        },
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '50%'
            }
        },
        dataLabels: {
            enabled: false
        },
        colors: ['#d3eafd', '#2196f3'], // Adjust colors for your needs
        series: [
            {
                name: '결제 완료',
                data: [50, 75, 60, 90, 100, 80, 70, 60, 85, 90, 70, 80] // Example data
            },
            {
                name: '취소',
                data: [20, 15, 25, 10, 15, 20, 25, 20, 15, 25, 30, 10] // Example data
            }
        ],
        xaxis: {
            type: 'category',
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },
        grid: {
            strokeDashArray: 4
        },
        tooltip: {
            theme: 'dark'
        }
    };

    var weeklyChart = new ApexCharts(document.querySelector('#weeklychart'), weeklyOptions);
    var monthlyChart = new ApexCharts(document.querySelector('#growthchart'), monthlyOptions);

    // Render both charts initially
    weeklyChart.render();
    monthlyChart.render();

    // Update the chart based on the selected option
    document.getElementById('chart-type-select').addEventListener('change', function(event) {
        var selectedValue = event.target.value;

        if (selectedValue === 'weekly') {
            document.getElementById('growthchart').style.display = 'none';
            document.getElementById('weekly-container').style.display = 'block';
        } else if (selectedValue === 'monthly') {
            document.getElementById('growthchart').style.display = 'block';
            document.getElementById('weekly-container').style.display = 'none';
        }
    });
}
