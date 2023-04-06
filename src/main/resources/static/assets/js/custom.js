////////////////
// Dental JS //
//////////////

$(document).ready(function () {

    const locale = {
        weekdays: {
            shorthand: [
                shorthandSunday,
                shorthandMonday,
                shorthandTuesday,
                shorthandWednesday,
                shorthandThursday,
                shorthandFriday,
                shorthandSaturday
            ],
            longhand: [
                longhandSunday,
                longhandMonday,
                longhandTuesday,
                longhandWednesday,
                longhandThursday,
                longhandFriday,
                longhandSaturday,
            ],
        },

        months: {
            shorthand: [
                shorthandJanuary,
                shorthandFebruary,
                shorthandMarch,
                shorthandApril,
                shorthandMay,
                shorthandJune,
                shorthandJuly,
                shorthandAugust,
                shorthandSeptember,
                shorthandOctober,
                shorthandNovember,
                shorthandDecember,
            ],
            longhand: [
                longhandJanuary,
                longhandFebruary,
                longhandMarch,
                longhandApril,
                longhandMay,
                longhandJune,
                longhandJuly,
                longhandAugust,
                longhandSeptember,
                longhandOctober,
                longhandNovember,
                longhandDecember,
            ],
        },

        firstDayOfWeek: firstDayOfWeek,

        ordinal: () => "",
    };

    flatpickr.localize(locale);

    $('#statisticsDaterange').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range',
        defaultDate: [new Date().fp_incr(-6), 'today'],
        maxDate: 'today',
    });

    $('#ordersDaterange').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range',
    });

    $('#usersRegDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#licenseDate').flatpickr({
        dateFormat: 'd.m.Y',
        minDate: 'today'
    });

    $('#userPaymentDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#userPaymentDate2').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#licensesPaymentDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#sellersAddDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#sellerPaymentDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#colorCreatedDate').flatpickr({
        dateFormat: 'd.m.Y',
        mode: 'range'
    });

    $('#addColorDate').flatpickr({
        dateFormat: 'd.m.Y',
        defaultDate: 'today'
    });

    $('#statisticsCountry').select2();

    $('#ordersPaymentType').select2({
        placeholder: typeof ordersListFilterPaymentType !== 'undefined' ? ordersListFilterPaymentType : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#ordersPaymentStatus').select2({
        placeholder: typeof ordersListFilterPaymentStatus !== 'undefined' ? ordersListFilterPaymentStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#ordersOrderStatus').select2({
        placeholder: typeof ordersListFilterOrderStatus !== 'undefined' ? ordersListFilterOrderStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#usersType').select2({
        placeholder: typeof usersListFilterType !== 'undefined' ? usersListFilterType : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#usersStatus').select2({
        placeholder: typeof usersListFilterStatus !== 'undefined' ? usersListFilterStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#licensesType').select2({
        placeholder: typeof licensesListFilterType !== 'undefined' ? licensesListFilterType : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#licensesStatus').select2({
        placeholder: typeof licensesListFilterStatus !== 'undefined' ? licensesListFilterStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#sellersType').select2({
        placeholder: typeof partnersListFilterType !== 'undefined' ? partnersListFilterType : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#sellersPercentage').select2({
        placeholder: typeof partnersListFilterPercentage !== 'undefined' ? partnersListFilterPercentage : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#sellersStatus').select2({
        placeholder: typeof partnersListFilterStatus !== 'undefined' ? partnersListFilterStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#addSellerType').select2({
        minimumResultsForSearch: -1
    });

    $('#colorZones').select2({
        placeholder: typeof shadeGuideNumberOfZones !== 'undefined' ? shadeGuideNumberOfZones : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('#colorStatus').select2({
        placeholder: typeof shadeGuideColorStatus !== 'undefined' ? shadeGuideColorStatus : '',
        allowClear: true,
        minimumResultsForSearch: -1
    });

    $('.colorType').select2({
        minimumResultsForSearch: -1
    });
});

const button = document.getElementById('clipboardBtn');

if (button) {
    const clipboard = new ClipboardJS(button);

    clipboard.on('success', function (e) {
        if (e.action == 'copy') {
            const currentText = button.innerHTML;

            if (button.innerHTML === 'Copied! <span class="ti ti-check ms-1"></span>') {
                return;
            }
            button.innerHTML = 'Copied! <span class="ti ti-check ms-1"></span>';

            setTimeout(function () {
                button.innerHTML = currentText;
            }, 2000)
        }
    })
}

const sortList = document.getElementById('sortList');
const sortList2 = document.getElementById('sortList2');

if (sortList) {
    Sortable.create(sortList, {
        handle: '.ti-grip-vertical',
        animation: 400
    });
}

if (sortList2) {
    Sortable.create(sortList2, {
        handle: '.ti-grip-vertical',
        animation: 400
    });
}

const ordersChart = document.getElementById('orders-chart');
const incomeChart = document.getElementById('income-chart');
const licensesChart = document.getElementById('licenses-chart');
const usersChart = document.getElementById('users-chart');
const sellersChart = document.getElementById('sellers-chart');
const shadeGuideChart = document.getElementById('shade-guide-chart');
const dentalNotationChart = document.getElementById('dental-notation-chart');
const ratingChart = document.getElementById('rating-chart');
const frequencyChart = document.getElementById('frequency-chart');
const analyzesChart = document.getElementById('analyzes-chart');
const analyzesTypeChart = document.getElementById('analyzesType-chart');

let objectOrderChart;
let objectIncomeChart;
let objectLicensesChart;
let objectUsersChart;
let objectSellersChart;
let objectShadeGuideChart;
let objectDentalNotationChart;
let objectRatingChart;
let objectFrequencyChart;
let objectAnalyzesChart;
let objectAnalyzesTypeChart;

if (ordersChart) {
    objectOrderChart = new Chart(ordersChart, {
        type: 'line',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: [
                {
                    label: 'Completed',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(115, 103, 240, 1)',
                    backgroundColor: 'rgba(115, 103, 240, 0.3)',
                    borderWidth: 1
                },
                {
                    label: 'Canceled',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(168, 170, 174, 1)',
                    backgroundColor: 'rgba(168, 170, 174, 0.3)',
                    borderWidth: 1
                }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (incomeChart) {
    objectIncomeChart = new Chart(incomeChart, {
        type: 'bar',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: [
                {
                    label: 'Completed',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(115, 103, 240, 1)',
                    backgroundColor: 'rgba(115, 103, 240, 0.3)'
                }]
        },
        options: {
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (licensesChart) {
    objectLicensesChart = new Chart(licensesChart, {
        type: 'bar',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: [
                {
                    label: 'Licenses payments',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(115, 103, 240, 1)',
                    backgroundColor: 'rgba(115, 103, 240, 0.3)'
                },
                {
                    label: 'Inactive licenses',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(168, 170, 174, 1)',
                    backgroundColor: 'rgba(168, 170, 174, 0.3)'
                }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (usersChart) {
    objectUsersChart = new Chart(usersChart, {
        type: 'line',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: [
                {
                    label: 'New registartions',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(115, 103, 240, 1)',
                    backgroundColor: 'rgba(115, 103, 240, 0.3)',
                    borderWidth: 1
                },
                {
                    label: 'Blocked',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(168, 170, 174, 1)',
                    backgroundColor: 'rgba(168, 170, 174, 0.3)',
                    borderWidth: 1
                }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (sellersChart) {
    objectSellersChart = new Chart(sellersChart, {
        type: 'line',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: [
                {
                    label: 'Dealers',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(115, 103, 240, 1)',
                    backgroundColor: 'rgba(115, 103, 240, 0.3)',
                    borderWidth: 1
                },
                {
                    label: 'Associations',
                    data: [],
                    fill: true,
                    borderColor: 'rgba(168, 170, 174, 1)',
                    backgroundColor: 'rgba(168, 170, 174, 0.3)',
                    borderWidth: 1
                }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (shadeGuideChart) {
    objectShadeGuideChart = new Chart(shadeGuideChart, {
        type: 'line',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: []
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (dentalNotationChart) {
    objectDentalNotationChart = new Chart(dentalNotationChart, {
        type: 'line',
        data: {
            labels: ['26/01', '27/01', '28/01', '29/01', '30/01', '31/01', '01/02'],
            datasets: []
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (ratingChart) {
    objectRatingChart = new Chart(ratingChart, {
        plugins: [ChartDataLabels],
        type: 'bar',
        data: {
            labels: ['Zone rating'],
            datasets: []
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            plugins: {
                datalabels: {
                    formatter: (value, analyzesChart) => {
                        let datapoints = analyzesChart.chart.data.datasets;
                        let max = 0;
                        datapoints.forEach((object) => {
                            let total = object.data.reduce((total, datapoint) => total + datapoint, 0);
                            max += total;
                        })
                        const percentage = value / max * 100;
                        return percentage.toFixed(2) + "%";
                    }
                }
            }
        }
    });
}

if (frequencyChart) {
    objectFrequencyChart = new Chart(frequencyChart, {
        type: 'bar',
        data: {
            labels: ['Frequency of tooth analysis (by FDI)'],
            datasets: []
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

if (analyzesChart) {
    objectAnalyzesChart = new Chart(analyzesChart, {
        plugins: [ChartDataLabels],
        type: 'pie',
        data: {
            labels: ['A1 => A2 Vita Classical', 'A1 => A3.5 Ivoclar Vivadent', 'A2 => A1 Vita Classical', 'A3.5 => A2 Vita Toothguide', 'A1.5 => A1 Ivoclar Vivadent'],
            datasets: [
                {
                    data: [],
                    fill: true,
                    backgroundColor: []
                }
            ]
        },
        options: {
            maintainAspectRatio: false,
            plugins: {
                datalabels: {
                    formatter: (value, analyzesChart) => {
                        const datapoints = analyzesChart.chart.data.datasets[0].data;
                        const total = datapoints.reduce((total, datapoint) => total + datapoint, 0);
                        const percentage = value / total * 100;
                        return percentage.toFixed(2) + "%";
                    }
                }
            }
        }
    });
}

if (analyzesTypeChart) {
    objectAnalyzesTypeChart = new Chart(analyzesTypeChart, {
        plugins: [ChartDataLabels],
        type: 'pie',
        data: {
            labels: ['Color measuring', 'Whitening'],
            datasets: [
                {
                    data: [],
                    fill: true,
                    backgroundColor: []
                }
            ]
        },
        options: {
            maintainAspectRatio: false,
            plugins: {
                datalabels: {
                    formatter: (value, analyzesChart) => {
                        const datapoints = analyzesChart.chart.data.datasets[0].data;
                        const total = datapoints.reduce((total, datapoint) => total + datapoint, 0);
                        const percentage = value / total * 100;
                        return percentage.toFixed(2) + "%";
                    }
                }
            }
        }
    });
}

let iti;
if (window.location.href.includes('/license/')) {
    const changePhone = document.getElementById("changePhone");

    iti = window.intlTelInput(changePhone, {
        preferredCountries: ['ua'],
        separateDialCode: true,
        customContainer: 'w-100',
        utilsScript: "https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/js/utils.js"
    });
}