document.addEventListener('DOMContentLoaded', function () {
    setTimeout(function () {
        floatchart();
    }, 500);
});

function getMaxValue(array) {
    return Math.max.apply(null, array);
}

// Define chart variables globally
var weeklyChart, monthlyChart;
document.getElementById('weeklychart').style.display = 'none';

function floatchart() {
    // 주간 데이터에서 최대값 찾기
    var maxWeeklyValue = Math.max(getMaxValue(weeklySales), getMaxValue(weeklyCanceledSales));

    // 월간 데이터에서 최대값 찾기
    var maxMonthlyValue = Math.max(getMaxValue(monthlySales), getMaxValue(monthlyCanceledSales));

    // 차트 높이 설정, 최대 510px로 제한
    var weeklyChartHeight = Math.min(maxWeeklyValue + 50, 510);
    var monthlyChartHeight = Math.min(maxMonthlyValue + 50, 510);

    // 주간 매출 차트 설정
    var weeklyOptions = {
        chart: {
            type: 'line',
            height: weeklyChartHeight, // 제한된 높이 사용
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
                data: weeklySales // 주간 매출 데이터
            },
            {
                name: '취소',
                data: weeklyCanceledSales // 주간 취소된 매출 데이터
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
            height: monthlyChartHeight, // 제한된 높이 사용
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
                data: monthlySales // 월간 매출 데이터
            },
            {
                name: '취소',
                data: monthlyCanceledSales // 월간 취소된 매출 데이터
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

    // Initialize charts
    weeklyChart = new ApexCharts(document.querySelector('#weeklychart'), weeklyOptions);
    monthlyChart = new ApexCharts(document.querySelector('#growthchart'), monthlyOptions);

    // Render charts initially
    weeklyChart.render();
    monthlyChart.render();

    // Event listener for the chart type select element
    document.getElementById('chart-type-select').addEventListener('change', function(event) {
        var selectedValue = event.target.value;

        if (selectedValue === 'weekly') {
            document.getElementById('growthchart').style.display = 'none';
            document.getElementById('weeklychart').style.display = 'block';
        } else if (selectedValue === 'monthly') {
            document.getElementById('growthchart').style.display = 'block';
            document.getElementById('weeklychart').style.display = 'none';
        }
    });
}
