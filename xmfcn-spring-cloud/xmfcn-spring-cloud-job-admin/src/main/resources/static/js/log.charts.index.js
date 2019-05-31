/**
 * Created by xuxueli on 17/4/24.
 */
$(function () {

    // filter Time
    var rangesConf = {};
    rangesConf[I18n.daterangepicker_ranges_today] = [moment().startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_yesterday] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_this_month] = [moment().startOf('month'), moment().endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_last_month] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_recent_week] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_recent_month] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

    $('#filterTime').daterangepicker({
        autoApply: false,
        singleDatePicker: false,
        showDropdowns: false,        // 是否显示年月选择条件
        timePicker: true, 			// 是否显示小时和分钟选择条件
        timePickerIncrement: 10, 	// 时间的增量，单位为分钟
        timePicker24Hour: true,
        opens: 'left', //日期选择框的弹出位置
        ranges: rangesConf,
        locale: {
            format: 'YYYY-MM-DD HH:mm:ss',
            separator: ' - ',
            customRangeLabel: I18n.daterangepicker_custom_name,
            applyLabel: I18n.system_ok,
            cancelLabel: I18n.system_cancel,
            fromLabel: I18n.daterangepicker_custom_starttime,
            toLabel: I18n.daterangepicker_custom_endtime,
            daysOfWeek: I18n.daterangepicker_custom_daysofweek.split(','),        // '日', '一', '二', '三', '四', '五', '六'
            monthNames: I18n.daterangepicker_custom_monthnames.split(','),        // '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
            firstDay: 1
        },
        startDate: rangesConf[I18n.daterangepicker_ranges_recent_month][0],
        endDate: rangesConf[I18n.daterangepicker_ranges_recent_month][1]
    }, function (start, end, label) {
        freshChartDate(start, end);
    });
    freshChartDate(rangesConf[I18n.daterangepicker_ranges_recent_month][0], rangesConf[I18n.daterangepicker_ranges_recent_month][1]);

    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshChartDate(startDate, endDate) {
        $.ajax({
            type: 'POST',
            url: base_url + '/logcharts/getLogLevelCharts',
            data: {
                'startTime': startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endTime': endDate.format('YYYY-MM-DD HH:mm:ss')
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    lineChartInit(data)
                    pieChartInit(data);
                } else {
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: (data.msg || I18n.job_dashboard_report_loaddata_fail),
                        icon: '2'
                    });
                }
            }
        });
    }

    /**
     * line Chart Init
     */
    function lineChartInit(data) {
        var option = {
            title: {
                text: "日志级别比例图"
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data: ["info", "error", "warn"]
            },
            toolbox: {
                feature: {
                    /*saveAsImage: {}*/
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: data.content.dayList
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: "infoCount",
                    type: 'line',
                    stack: 'Total',
                    areaStyle: {normal: {}},
                    data: data.content.dayCountInfoList
                },
                {
                    name: "errorCount",
                    type: 'line',
                    stack: 'Total',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: data.content.dayCountErrorList
                },
                {
                    name: "warnCount",
                    type: 'line',
                    stack: 'Total',
                    areaStyle: {normal: {}},
                    data: data.content.dayCountWarnList
                },
                {
                    name: "debugCount",
                    type: 'line',
                    stack: 'Total',
                    areaStyle: {normal: {}},
                    data: data.content.dayCountDebugList
                }
            ],
            color: ['#00A65A', '#c23632', '#F39C12']
        };

        var lineChart = echarts.init(document.getElementById('lineChart'));
        lineChart.setOption(option);
    }

    /**
     * pie Chart Init
     */
    function pieChartInit(data) {
        var option = {
            title: {
                text: I18n.job_dashboard_rate_report,
                /*subtext: 'subtext',*/
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ["info", "error", "warn","debug"]
            },
            series: [
                {
                    //name: '分布比例',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {
                            name: "info",
                            value: data.content.infoCount
                        },
                        {
                            name: "error",
                            value: data.content.errorCount
                        },
                        {
                            name: "warn",
                            value: data.content.warnCount
                        },
                        {
                            name: "debug",
                            value: data.content.debugCount
                        }
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
            color: ['#00A65A', '#c23632', '#F39C12']
        };
        var pieChart = echarts.init(document.getElementById('pieChart'));
        pieChart.setOption(option);
    }

});
