window.appConfig = {
    // 测试用例数据
    testCases: [
        { 
            value: 'test1', 
            key: 'login',
            label: '登录测试',
            description: '用户登录功能相关测试用例'
        },
        { 
            value: 'test2', 
            key: 'payment',
            label: '支付测试',
            description: '支付流程相关测试用例'
        },
        { 
            value: 'test3', 
            label: '注册测试',
            description: '用户注册流程测试用例'
        },
        { 
            value: 'test4', 
            label: '商品测试',
            description: '商品相关功能测试用例'
        }
    ],
    
    // 线程数据映射表
    threadMap: {
        test1: [
            { value: 'thread1_1', label: '主线程' },
            { value: 'thread1_2', label: '网络线程' },
            { value: 'thread1_3', label: '渲染线程' }
        ],
        test2: [
            { value: 'thread2_1', label: '支付主线程' },
            { value: 'thread2_2', label: '安全验证线程' }
        ],
        test3: [
            { value: 'thread3_1', label: '注册线程' },
            { value: 'thread3_2', label: '验证码线程' }
        ],
        test4: [
            { value: 'thread4_1', label: '商品加载线程' },
            { value: 'thread4_2', label: '缓存线程' }
        ]
    }
}; 