class FlameGraphManager {
    constructor() {
        this.chart = null;
        this.currentData = null;
        this.renderItem = this.renderItem.bind(this);
        this.clickCallback = null;
        this.currentTestCase = null;
    }

    async loadTestData(testCase) {
        if (this.currentTestCase === testCase) {
            return window.testData;
        }
        
        try {
            // 移除旧的脚本（如果存在）
            const oldScript = document.getElementById('test-data-script');
            if (oldScript) {
                oldScript.remove();
            }
            
            // 加载新的数据文件
            return new Promise((resolve, reject) => {
                const script = document.createElement('script');
                script.id = 'test-data-script';
                script.src = `data/${testCase}.js`;
                script.onload = () => {
                    this.currentTestCase = testCase;
                    resolve(window.testData);
                };
                script.onerror = reject;
                document.head.appendChild(script);
            });
        } catch (error) {
            console.error('Error loading test data:', error);
            throw error;
        }
    }

    init(element) {
        this.chart = echarts.init(element, null, {
            renderer: 'canvas',
            useDirtyRect: false
        });
        this.initEvents();
        this.updateChart();
    }

    initEvents() {
        // 处理点击事件
        this.chart.on('click', (params) => {
            if (this.currentData && params.data.name) {
                const data = this.recursionJson(this.currentData, params.data.name);
                const rootValue = data[0].value[2];
                this.chart.setOption({
                    xAxis: { max: rootValue },
                    series: [{ data }]
                });
                
                // 调用点击回调
                if (this.clickCallback) {
                    this.clickCallback(params.data);
                }
            }
        });

        // 处理窗口大小变化
        window.addEventListener('resize', () => this.chart.resize());
    }

    // 颜色映射
    ColorTypes = {
        root: '#8fd3e8',
        genunix: '#d95850',
        unix: '#eb8146',
        ufs: '#ffb248',
        FSS: '#f2d643',
        namefs: '#ebdba4',
        doorfs: '#fcce10',
        lofs: '#b5c334',
        zfs: '#1bca93'
    };

    // 过滤JSON数据
    filterJson(json, id) {
        if (id == null) {
            return json;
        }
        const recur = (item, id) => {
            if (item.id === id) {
                return item;
            }
            for (const child of item.children || []) {
                const temp = recur(child, id);
                if (temp) {
                    item.children = [temp];
                    item.value = temp.value;
                    return item;
                }
            }
        };
        return recur(json, id) || json;
    }

    // 递归处理JSON数据
    recursionJson(jsonObj, id) {
        const data = [];
        const filteredJson = this.filterJson(JSON.parse(JSON.stringify(jsonObj)), id);
        const rootVal = filteredJson.value;
        
        const recur = (item, start = 0, level = 0) => {
            const temp = {
                name: item.id,
                value: [
                    level,
                    start,
                    start + item.value,
                    item.name,
                    (item.value / rootVal) * 100
                ],
                className: item.className,
                itemStyle: {
                    color: this.ColorTypes[item.name.split(' ')[0]]
                }
            };
            data.push(temp);
            let prevStart = start;
            for (const child of item.children || []) {
                recur(child, prevStart, level + 1);
                prevStart = prevStart + child.value;
            }
        };
        recur(filteredJson);
        return data;
    }

    // 计算JSON数据的高度
    heightOfJson(json) {
        const recur = (item, level = 0) => {
            if ((item.children || []).length === 0) {
                return level;
            }
            let maxLevel = level;
            for (const child of item.children) {
                const tempLevel = recur(child, level + 1);
                maxLevel = Math.max(maxLevel, tempLevel);
            }
            return maxLevel;
        };
        return recur(json);
    }

    // 渲染项目
    renderItem(params, api) {
        const level = api.value(0);
        const start = api.coord([api.value(1), level]);
        const end = api.coord([api.value(2), level]);
        const height = ((api.size && api.size([0, 1])) || [0, 20])[1];
        const width = end[0] - start[0];
        
        return {
            type: 'rect',
            transition: ['shape'],
            shape: {
                x: start[0],
                y: start[1] - height / 2,
                width,
                height: height - 2,
                r: 2
            },
            style: {
                fill: api.visual('color')
            },
            emphasis: {
                style: {
                    stroke: '#000'
                }
            },
            textConfig: {
                position: 'insideLeft'
            },
            textContent: {
                style: {
                    text: api.value(3),
                    fontFamily: 'Verdana',
                    fill: '#000',
                    width: width - 4,
                    overflow: 'truncate',
                    ellipsis: '..',
                    truncateMinChar: 1
                },
                emphasis: {
                    style: {
                        stroke: '#000',
                        lineWidth: 0.5
                    }
                }
            }
        };
    }

    clearChart() {
        if (this.chart) {
            this.chart.clear();
            // 显示提示文字
            this.chart.setOption({
                title: {
                    text: '请选择测试用例和线程',
                    left: 'center',
                    top: '45%',
                    textStyle: {
                        fontSize: 20,
                        color: '#909399'
                    }
                }
            });
        }
    }

    async updateChart(testCase, thread) {
        try {
            // 确保有测试用例和线程被选中
            if (!testCase || !thread) {
                this.clearChart();
                return;
            }

            // 加载对应测试用例的数据文件
            const testData = await this.loadTestData(testCase);
            // 获取选中线程的数据
            const flameData = testData[thread];
            this.currentData = flameData;
            const levelOfJson = this.heightOfJson(flameData);

            const option = {
                backgroundColor: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0.05,
                            color: '#eee'
                        },
                        {
                            offset: 0.95,
                            color: '#eeeeb0'
                        }
                    ]
                },
                tooltip: {
                    formatter: (params) => {
                        const samples = params.value[2] - params.value[1];
                        // 将纳秒转换为更易读的时间格式
                        const formatTime = (ns) => {
                            if (ns < 1000) return `${ns}ns`;
                            if (ns < 1000000) return `${(ns/1000).toFixed(2)}μs`;
                            if (ns < 1000000000) return `${(ns/1000000).toFixed(2)}ms`;
                            return `${(ns/1000000000).toFixed(2)}s`;
                        };
                        
                        const details = [
                            `<div style="font-weight: bold;">${params.value[3]}</div>`,
                            `<div style="margin-top: 4px; color: #409EFF; font-family: monospace;">`,
                            `${params.data.className || 'Unknown'}`,
                            `</div>`,
                            `<div style="margin-top: 8px;">`,
                            `执行时间: ${formatTime(samples)}`,
                            `<br/>占比: ${+params.value[4].toFixed(2)}%`,
                            `</div>`
                        ];
                        
                        return details.join('<br/>');
                    },
                    backgroundColor: 'rgba(255, 255, 255, 0.85)',
                    borderColor: '#eee',
                    borderWidth: 1,
                    padding: [8, 12],
                    textStyle: {
                        color: '#666',
                        fontSize: 12
                    },
                    extraCssText: 'box-shadow: 0 2px 8px rgba(0,0,0,0.15);'
                },
                animationDuration: 300,
                animationEasing: 'cubicOut',
                title: {
                    text: '函数调用火焰图',
                    left: 'center',
                    top: 10,
                    textStyle: {
                        fontFamily: 'Verdana',
                        fontWeight: 'normal',
                        fontSize: 20
                    }
                },
                toolbox: {
                    feature: {
                        restore: {},
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        saveAsImage: {
                            pixelRatio: 2
                        }
                    },
                    right: 20,
                    top: 10
                },
                dataZoom: [
                    {
                        type: 'slider',
                        show: true,
                        xAxisIndex: [0],
                        start: 0,
                        end: 100,
                        bottom: 10,
                        height: 20,
                        borderColor: '#ccc',
                        textStyle: {
                            color: '#666'
                        }
                    },
                    {
                        type: 'inside',
                        xAxisIndex: [0],
                        start: 0,
                        end: 100,
                        zoomOnMouseWheel: true,
                        moveOnMouseMove: true,
                        zoomLock: false,
                        throttle: 100
                    }
                ],
                progressive: 500,
                progressiveThreshold: 3000,
                xAxis: {
                    show: false,
                    type: 'value',
                    max: flameData.value
                },
                yAxis: {
                    show: false,
                    type: 'category',
                    max: levelOfJson
                },
                series: [
                    {
                        type: 'custom',
                        renderItem: this.renderItem,
                        encode: {
                            x: [1, 2],
                            y: 0
                        },
                        data: this.recursionJson(flameData),
                        large: true,
                        largeThreshold: 500
                    }
                ]
            };

            console.log('Setting chart option:', option);
            this.chart.setOption(option, true);
        } catch (error) {
            console.error('Error updating chart:', error);
            this.clearChart();
        }
    }

    // 添加点击回调设置方法
    onNodeClick(callback) {
        this.clickCallback = callback;
    }
}

// 将实例挂载到 window 对象上
window.chartManager = new FlameGraphManager(); 