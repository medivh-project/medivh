const testCasesData = {
    "testCases": [
        {
            "name": "Login Test Suite",
            "threads": [
                {
                    "name": "main-thread-1",
                    "functionRoot": {
                        "name": "com.example.user.UserController.handleRequest",
                        "value": 5200,
                        "invokeCount": 1500,
                        "avgCost": 3.47,
                        "expectTime": 3.0,
                        "startTime": 1648689120000000000,
                        "endTime": 1648689125200000000,
                        "children": [
                            {
                                "name": "com.example.user.UserService.authenticate",
                                "value": 1800,
                                "invokeCount": 1500,
                                "avgCost": 1.2,
                                "expectTime": 1.0,
                                "startTime": 1648689120100000000,
                                "endTime": 1648689121900000000,
                                "children": [
                                    {
                                        "name": "com.example.auth.AuthenticationService.validate",
                                        "value": 1200,
                                        "invokeCount": 1500,
                                        "avgCost": 0.8,
                                        "expectTime": 0.5,
                                        "startTime": 1648689120200000000,
                                        "endTime": 1648689121400000000,
                                        "children": [
                                            {
                                                "name": "com.example.auth.TokenValidator.checkToken",
                                                "value": 600,
                                                "invokeCount": 1500,
                                                "avgCost": 0.4,
                                                "expectTime": 0.3,
                                                "startTime": 1648689120300000000,
                                                "endTime": 1648689120900000000
                                            },
                                            {
                                                "name": "com.example.auth.PermissionChecker.verify",
                                                "value": 400,
                                                "invokeCount": 1500,
                                                "avgCost": 0.27,
                                                "expectTime": 0.2,
                                                "startTime": 1648689121000000000,
                                                "endTime": 1648689121400000000
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "name": "com.example.user.UserService.getUserInfo",
                                "value": 2200,
                                "invokeCount": 1500,
                                "avgCost": 1.47,
                                "expectTime": 1.0,
                                "startTime": 1648689122000000000,
                                "endTime": 1648689124200000000,
                                "children": [
                                    {
                                        "name": "com.example.db.UserRepository.findById",
                                        "value": 1400,
                                        "invokeCount": 1500,
                                        "avgCost": 0.93,
                                        "expectTime": 0.8,
                                        "startTime": 1648689122100000000,
                                        "endTime": 1648689123500000000,
                                        "children": [
                                            {
                                                "name": "org.hibernate.UserEntity.load",
                                                "value": 900,
                                                "invokeCount": 1500,
                                                "avgCost": 0.6,
                                                "expectTime": 0.5,
                                                "startTime": 1648689122200000000,
                                                "endTime": 1648689123100000000
                                            }
                                        ]
                                    },
                                    {
                                        "name": "com.example.cache.UserCache.get",
                                        "value": 500,
                                        "invokeCount": 1500,
                                        "avgCost": 0.33,
                                        "expectTime": 0.2,
                                        "startTime": 1648689123600000000,
                                        "endTime": 1648689124100000000
                                    }
                                ]
                            },
                            {
                                "name": "com.example.user.ResponseBuilder.build",
                                "value": 1200,
                                "invokeCount": 1500,
                                "avgCost": 0.8,
                                "expectTime": 1.0,
                                "startTime": 1648689124300000000,
                                "endTime": 1648689125500000000,
                                "children": [
                                    {
                                        "name": "com.example.serializer.JsonSerializer.toJson",
                                        "value": 800,
                                        "invokeCount": 1500,
                                        "avgCost": 0.53,
                                        "expectTime": 0.5,
                                        "startTime": 1648689124400000000,
                                        "endTime": 1648689125200000000
                                    }
                                ]
                            }
                        ]
                    }
                },
                {
                    "name": "async-thread-1",
                    "functionRoot": {
                        "name": "com.example.user.UserController.handleRequest",
                        "value": 5200,
                        "invokeCount": 1500,
                        "avgCost": 3.47,
                        "expectTime": 3.0,
                        "startTime": 1648689120000000000,
                        "endTime": 1648689125200000000,
                        "children": []
                    }
                }
            ]
        },
        {
            "name": "Payment Test Suite",
            "threads": [
                {
                    "name": "main-thread-2",
                    "functionRoot": {
                        "name": "com.example.user.UserController.handleRequest",
                        "value": 5200,
                        "invokeCount": 1500,
                        "avgCost": 3.47,
                        "expectTime": 3.0,
                        "startTime": 1648689120000000000,
                        "endTime": 1648689125200000000,
                        "children": []
                    }
                }
            ]
        }
    ]
};
