class FlameGraphManager {
    constructor() {
        this.chart = null;
        this.currentTestData = null;
        this.renderItem = this.renderItem.bind(this);
        this.clickCallback = null;
    }

    static getTestCases() {
        return testCasesData.map((testCase, index) => ({
            value: `test${index}`,
            label: testCase.name,
            description: `测试用例 ${testCase.name} 的函数调用数据`
        }));
    }

    static getThreads(testCaseValue) {
        const testIndex = parseInt(testCaseValue.replace('test', ''));
        const testCase = testCasesData[testIndex];
        if (!testCase) return [];
        
        return testCase.threads.map((thread, index) => ({
            value: `${testCaseValue}_thread${index}`,
            label: thread.name
        }));
    }

    static getThreadData(testCaseValue, threadValue) {
        const testIndex = parseInt(testCaseValue.replace('test', ''));
        const threadIndex = parseInt(threadValue.split('_thread')[1]);
        const testCase = testCasesData[testIndex];
        if (!testCase) return null;
        
        const thread = testCase.threads[threadIndex];
        if (!thread) return null;

        return thread.functionRoot;
    }

    init(element) {
        this.chart = echarts.init(element, null, {
            renderer: 'canvas',
            useDirtyRect: false
        });
        this.initEvents();
        this.clearChart();
    }

    initEvents() {
        this.chart.on('click', (params) => {
            if (this.currentTestData && params.data.name) {
                const chartData = this.processTreeData(this.currentTestData, this.currentTestData.value);
                
                this.chart.setOption({
                    xAxis: { max: this.currentTestData.value },
                    series: [{ data: chartData }]
                });
                
                if (this.clickCallback && params.value[0] !== 0) {
                    this.clickCallback({
                        name: params.data.name,
                        count: params.data.count,
                        percentage: params.value[4],
                        id: params.data.id
                    });
                }
            }
        });

        window.addEventListener('resize', () => this.chart.resize())
    }

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

    renderItem(params, api) {
        const level = api.value(0);
        const start = api.coord([api.value(1), level]);
        const end = api.coord([api.value(2), level]);
        const height = api.size([0, 1])[1] * 0.7;
        const width = end[0] - start[0];

        const baseColor = api.visual('color');
        const lighterColor = this.getLighterColor(baseColor);
        const darkerColor = this.getDarkerColor(baseColor);

        return {
            type: 'rect',
            transition: ['shape'],
            shape: {
                x: start[0],
                y: start[1] - height / 2,
                width,
                height,
                r: 1
            },
            style: {
                fill: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                        offset: 0,
                        color: lighterColor
                    }, {
                        offset: 0.5,
                        color: baseColor
                    }, {
                        offset: 1,
                        color: darkerColor
                    }]
                },
                stroke: '#fff',
                lineWidth: 0.5,
                shadowBlur: 2,
                shadowColor: 'rgba(0,0,0,0.2)'
            },
            textConfig: {
                position: width > 60 ? 'insideLeft' : 'right'
            },
            textContent: {
                style: {
                    text: api.value(3),
                    fill: width > 60 ? '#fff' : '#666',
                    fontSize: 12,
                    fontFamily: 'Consolas, monospace',
                    textShadowColor: width > 60 ? 'rgba(0,0,0,0.5)' : 'none',
                    textShadowBlur: width > 60 ? 2 : 0,
                    width: width - 4,
                    overflow: 'truncate',
                    ellipsis: '..'
                }
            }
        };
    }

    clearChart() {
        if (this.chart) {
            this.chart.clear();
            this.chart.setOption({
                title: {
                    text: window.currentLang === 'en' ? 'Please select a test case and thread' : '请选择测试用例和线程',
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

    processTreeData(node, totalDuration) {
        const data = [];
        const processNode = (node, level = 0, start = 0) => {
            const value = node.value || node.duration;
            const percentage = (value / totalDuration) * 100;
            const count = node.count || 0;
            
            data.push({
                name: node.name,
                id: `${node.className}.${node.name}`,
                className: node.className,
                count: count,
                value: [level, start, start + value, node.name, percentage],
                itemStyle: {
                    color: this.getNodeColor(node.className)
                }
            });

            let currentStart = start;
            if (node.children && node.children.length > 0) {
                node.children.forEach(child => {
                    processNode(child, level + 1, currentStart);
                    currentStart += child.value || child.duration;
                });
            }
        };

        processNode(node);
        return data;
    }

    getNodeColor(className) {
        const baseHues = {
            'tech.medivh.demo.kotlin.DemoClass': 200,
            'tech.medivh.demo.kotlin.DemoClass1': 120,
            'tech.medivh.demo.kotlin.DemoClass2': 280,
            'medivh': 0
        };

        let baseHue = 0;
        for (const [prefix, hue] of Object.entries(baseHues)) {
            if (className.startsWith(prefix)) {
                baseHue = hue;
                break;
            }
        }

        let hash = 0;
        for (let i = 0; i < className.length; i++) {
            hash = className.charCodeAt(i) + ((hash << 5) - hash);
        }
        
        const hueOffset = (hash % 60) - 30;
        const hue = (baseHue + hueOffset + 360) % 360;
        
        const saturation = 65 + (hash % 20);
        const lightness = 45 + (hash % 15);

        return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
    }

    async updateChart(testCase, thread) {
        try {
            const threadData = FlameGraphManager.getThreadData(testCase, thread);
            if (!threadData) {
                this.clearChart();
                return;
            }

            this.currentTestData = threadData;
            const chartData = this.processTreeData(threadData, threadData.value);

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
                        const formatTime = (ns) => {
                            if (ns < 1000) return `${ns}ns`;
                            if (ns < 1_000_000) return `${(ns/1000).toFixed(2)}μs`;
                            if (ns < 1_000_000_000) return `${(ns/1_000_000).toFixed(2)}ms`;
                            return `${(ns/1_000_000_000).toFixed(2)}s`;
                        };
                        
                        const lang = window.currentLang || 'zh';
                        const labels = {
                            zh: {
                                totalTime: '总执行时间',
                                class: '所属类',
                                execTime: '执行时间',
                                callCount: '调用次数',
                                percentage: '占比',
                                times: '次'
                            },
                            en: {
                                totalTime: 'Total Execution Time',
                                class: 'Class',
                                execTime: 'Execution Time',
                                callCount: 'Call Count',
                                percentage: 'Percentage',
                                times: 'times'
                            }
                        }[lang];
                        
                        if (params.value[0] === 0) {
                            return `
                                <div style="padding: 3px 0">
                                    <div style="font-weight: bold; font-size: 14px; margin-bottom: 8px;">
                                        ${labels.totalTime}
                                    </div>
                                    <div style="color: #409EFF; font-size: 16px; font-family: monospace;">
                                        ${formatTime(samples)}
                                    </div>
                                </div>`;
                        }
                        
                        return `
                            <div style="padding: 3px 0">
                                <div style="font-weight: bold; font-size: 14px; margin-bottom: 8px;">
                                    ${params.value[3]}
                                </div>
                                <div style="display: grid; grid-template-columns: auto 1fr; gap: 8px; font-size: 13px;">
                                    <div style="color: #666;">${labels.class}:</div>
                                    <div style="color: #409EFF; font-family: monospace;">${params.data.className}</div>
                                    <div style="color: #666;">${labels.execTime}:</div>
                                    <div style="color: #333; font-family: monospace;">${formatTime(samples)}</div>
                                    <div style="color: #666;">${labels.callCount}:</div>
                                    <div style="color: #333; font-family: monospace;">${params.data.count}</div>
                                    <div style="color: #666;">${labels.percentage}:</div>
                                    <div style="color: #333; font-family: monospace;">${params.value[4].toFixed(2)}%</div>
                                </div>
                            </div>`;
                    },
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    borderColor: '#eee',
                    borderWidth: 1,
                    padding: [12, 16],
                    textStyle: {
                        color: '#333',
                        fontSize: 13
                    },
                    extraCssText: 'box-shadow: 0 2px 12px rgba(0,0,0,0.1); border-radius: 4px;'
                },
                animationDuration: 300,
                animationEasing: 'cubicOut',
                title: {
                    text: window.currentLang === 'en' ? 'Function Call Flame Graph' : '函数调用火焰图',
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
                        },
                        zoomLock: true,
                        moveOnMouseMove: false,
                        zoomOnMouseWheel: false,
                        preventDefaultMouseMove: true
                    }
                ],
                progressive: 500,
                progressiveThreshold: 3000,
                xAxis: {
                    show: false,
                    type: 'value',
                    max: threadData.value
                },
                yAxis: {
                    show: false,
                    type: 'category'
                },
                series: [
                    {
                        type: 'custom',
                        renderItem: this.renderItem,
                        encode: {
                            x: [1, 2],
                            y: 0
                        },
                        data: chartData,
                        large: true,
                        largeThreshold: 500
                    }
                ]
            };

            this.chart.setOption(option, true);
        } catch (error) {
            console.error('Error updating chart:', error);
            this.clearChart();
        }
    }

    onNodeClick(callback) {
        this.clickCallback = callback;
    }

    getLighterColor(hslColor) {
        const matches = hslColor.match(/hsl\((\d+),\s*(\d+)%,\s*(\d+)%\)/);
        if (matches) {
            const h = matches[1];
            const s = matches[2];
            const l = Math.min(parseInt(matches[3]) + 15, 85);
            return `hsl(${h}, ${s}%, ${l}%)`;
        }
        return hslColor;
    }

    getDarkerColor(hslColor) {
        const matches = hslColor.match(/hsl\((\d+),\s*(\d+)%,\s*(\d+)%\)/);
        if (matches) {
            const h = matches[1];
            const s = matches[2];
            const l = Math.max(parseInt(matches[3]) - 10, 25);
            return `hsl(${h}, ${s}%, ${l}%)`;
        }
        return hslColor;
    }
}

window.chartManager = new FlameGraphManager();
window.getTestCases = FlameGraphManager.getTestCases;
window.getThreads = FlameGraphManager.getThreads; 
