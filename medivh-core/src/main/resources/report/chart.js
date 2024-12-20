class FlameGraphManager {
    constructor() {
        this.chart = null;
        this.currentTestData = null;
        this.renderItem = this.renderItem.bind(this);
        this.clickCallback = null;
        this.nodeMap = new Map();
        this.currentTestCase = null;
        this.currentThread = null;
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
            if (this.currentTestData) {
                const data = this.processTreeData(this.currentTestData, params.data.name);
                const rootValue = data[0].value[2];
                
                this.chart.setOption({
                    xAxis: { max: rootValue },
                    series: [{ data }]
                });
                
                if (this.clickCallback && params.value[0] !== 0) {
                    const callStack = this.getCallStack(params.data.name);
                    
                    const testIndex = parseInt(this.currentTestCase.replace('test', ''));
                    const threadIndex = parseInt(this.currentThread.split('_thread')[1]);
                    const thread = testCasesData[testIndex].threads[threadIndex];
                    const totalStats = thread.aggregation.threadTotalInvoke[params.data.name];

                    const durationInStack = params.value[2] - params.value[1];

                    const nodeData = {
                        name: params.value[3],
                        count: params.data.count,
                        percentage: params.value[4],
                        className: params.data.className,
                        callStack: callStack,
                        value: params.data.value,
                        durationInStack: durationInStack,
                        totalCount: totalStats?.invokeCount || 0,
                        totalValue: totalStats?.totalCost || 0,
                        maxValue: totalStats?.maxCost || 0,
                        minValue: totalStats?.minCost || 0,
                        avgValue: totalStats ? Math.floor(totalStats.totalCost / totalStats.invokeCount) : 0
                    };

                    this.clickCallback(nodeData);
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
            if (!item.children || item.children.length === 0) {
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
        const baseHeight = Math.min(((api.size && api.size([0, 1])) || [0, 40])[1], 40);
        const height = baseHeight * 0.6;
        const width = end[0] - start[0];
        
        const y = api.getHeight() - (level + 1) * height;
        
        return {
            type: 'rect',
            transition: ['shape'],
            shape: {
                x: start[0],
                y: y,
                width,
                height: height,
                r: 1
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

    processTreeData(jsonObj, id) {
        const data = [];
        const filteredJson = this.filterJson(structuredClone(jsonObj), id);
        const rootVal = filteredJson.value;
        
        const levelIndices = new Map();
        
        const recur = (item, start = 0, level = 0) => {
            const index = levelIndices.get(level) || 0;
            levelIndices.set(level, index + 1);
            
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
                count: item.count,
                maxValue: item.maxValue,
                minValue: item.minValue,
                itemStyle: {
                    color: this.getNodeColor(item.className, level, index)
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

    getNodeColor(className, level, index) {
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
        
        const hueOffset = ((hash + level + index) % 5) * 72;
        const hue = (baseHue + hueOffset) % 360;
        
        const saturation = 60 + ((hash + index) % 20);
        const lightness = 45 + ((hash + level) % 15);

        return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
    }

    async updateChart(testCase, thread) {
        try {
            this.currentTestCase = testCase;
            this.currentThread = thread;
            const threadData = FlameGraphManager.getThreadData(testCase, thread);
            if (!threadData) {
                this.clearChart();
                return;
            }

            this.currentTestData = threadData;
            this.buildNodeMap(threadData);
            const levelOfOriginalJson = this.heightOfJson(threadData);
            const chartData = this.processTreeData(threadData);

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
                        const duration = params.value[2] - params.value[1];
                        const formatTime = (ns) => {
                            if (ns >= 1_000_000_000) {
                                return `${(ns/1_000_000_000).toFixed(2)}s`;
                            } else if (ns >= 1_000_000) {
                                return `${(ns/1_000_000).toFixed(2)}ms`;
                            } else if (ns >= 1000) {
                                return `${(ns/1000).toFixed(2)}μs`;
                            } else {
                                return `${ns}ns`;
                            }
                        };

                        const lang = window.currentLang || 'zh';
                        const labels = {
                            zh: {
                                execTime: '执行时间',
                                percentage: '占比',
                                className: '函数所在类',
                                callCount: '在同级调用栈的执行次数',
                                times: '次'
                            },
                            en: {
                                execTime: 'Execution Time',
                                percentage: 'Percentage',
                                className: 'Class Name',
                                callCount: 'Call Count in Same Stack Level',
                                times: 'times'
                            }
                        }[lang];

                        return `${params.marker} ${params.value[3]}<br/>
                                ${labels.className}: ${params.data.className}<br/>
                                ${labels.callCount}: ${params.data.count} ${labels[lang === 'en' ? 'times' : 'times']}<br/>
                                ${labels.execTime}: ${formatTime(duration)}<br/>
                                ${labels.percentage}: ${params.value[4].toFixed(2)}%`;
                    }
                },
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
                        restore: {}
                    },
                    right: 20,
                    top: 10
                },
                xAxis: {
                    show: false
                },
                yAxis: {
                    show: false,
                    max: levelOfOriginalJson
                },
                grid: {
                    top: 60,
                    bottom: 20,
                    left: 10,
                    right: 10,
                    containLabel: true
                },
                series: [
                    {
                        type: 'custom',
                        renderItem: this.renderItem,
                        encode: {
                            x: [0, 1, 2],
                            y: 0
                        },
                        data: chartData,
                        gap: 1
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

    buildNodeMap(threadData) {
        this.nodeMap.clear();
        const stack = [threadData];
        while (stack.length > 0) {
            const node = stack.pop();
            if (node.id) {
                this.nodeMap.set(node.id, node);
            }
            if (node.children) {
                stack.push(...node.children);
            }
        }
    }

    getCallStack(nodeId) {
        const callStack = [];
        let currentNode = this.nodeMap.get(nodeId);
        
        while (currentNode) {
            callStack.unshift({
                name: currentNode.name,
                className: currentNode.className,
                value: currentNode.value,
                count: currentNode.count,
                level: callStack.length
            });
            currentNode = this.nodeMap.get(currentNode.parentId);
        }
        
        return callStack;
    }
}

window.chartManager = new FlameGraphManager();
window.getTestCases = FlameGraphManager.getTestCases;
window.getThreads = FlameGraphManager.getThreads; 

