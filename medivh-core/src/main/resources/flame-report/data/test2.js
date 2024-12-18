window.testData = {
    thread2_1: {
        // 支付主线程数据
        name: "root",
        value: 48923,
        className: "com.payment.main.PaymentThread",
        id: "4a7b9c2d-5e3f-41a8-b6d9-2c8f7e4d5b3a",
        children: [
            {
                name: "payment process_transaction",
                id: "3b6a8d2c-7e4f-49b5-a3d8-1c9e7f6d5b4a",
                value: 25678,
                className: "com.payment.transaction.TransactionProcessor",
                children: [
                    {
                        name: "payment validate_order",
                        id: "2c5d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                        value: 12345,
                        className: "com.payment.order.OrderValidator",
                        children: [
                            {
                                name: "payment check_inventory",
                                id: "1d4c7b6a-8e5f-42d9-b3c8-9e7f6d5c4b3a",
                                value: 6789,
                                className: "com.payment.inventory.InventoryChecker"
                            },
                            {
                                name: "payment verify_price",
                                id: "0e3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                                value: 4567,
                                className: "com.payment.price.PriceVerifier"
                            }
                        ]
                    },
                    {
                        name: "payment process_payment",
                        id: "9b6a8d2c-7e4f-49b5-a3d8-1c9e7f6d5b4a",
                        value: 10234,
                        className: "com.payment.processor.PaymentProcessor",
                        children: [
                            {
                                name: "payment call_bank_api",
                                id: "8c5d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                                value: 7890,
                                className: "com.payment.bank.BankApiClient"
                            }
                        ]
                    }
                ]
            },
            {
                name: "payment update_order_status",
                id: "7d2e5f1a-8c3b-45a9-b2d6-9f8e4c7d2b1a",
                value: 15678,
                className: "com.payment.order.OrderStatusUpdater",
                children: [
                    {
                        name: "payment write_database",
                        id: "6e3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                        value: 8901,
                        className: "com.payment.database.DatabaseWriter",
                        children: [
                            {
                                name: "payment execute_transaction",
                                id: "5f2e1d4c-7b6a-45d9-b3c8-9e7f6d5c4b3a",
                                value: 5678,
                                className: "com.payment.database.TransactionExecutor"
                            }
                        ]
                    }
                ]
            }
        ]
    },
    thread2_2: {
        // 安全验证线程数据
        name: "root",
        value: 31567,
        className: "com.payment.security.SecurityThread",
        id: "2d5e8f4c-9b3a-47d6-c5e9-8f7d6e5c4b3a",
        children: [
            {
                name: "security verify_signature",
                id: "1c4d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                value: 18934,
                className: "com.payment.security.SignatureVerifier",
                children: [
                    {
                        name: "security decrypt_data",
                        id: "0b3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                        value: 12345,
                        className: "com.payment.security.DataDecryptor",
                        children: [
                            {
                                name: "security load_keys",
                                id: "9a2e1d4c-7b6a-45d9-b3c8-9e7f6d5c4b3a",
                                value: 5678,
                                className: "com.payment.security.KeyLoader"
                            },
                            {
                                name: "security perform_decryption",
                                id: "8b3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                                value: 4567,
                                className: "com.payment.security.Decryptor"
                            }
                        ]
                    }
                ]
            },
            {
                name: "security risk_analysis",
                id: "7c4d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                value: 10234,
                className: "com.payment.security.RiskAnalyzer",
                children: [
                    {
                        name: "security check_blacklist",
                        id: "6b3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                        value: 5678,
                        className: "com.payment.security.BlacklistChecker",
                        children: [
                            {
                                name: "security query_database",
                                id: "5a2e1d4c-7b6a-45d9-b3c8-9e7f6d5c4b3a",
                                value: 3456,
                                className: "com.payment.security.DatabaseQuerier"
                            }
                        ]
                    },
                    {
                        name: "security analyze_behavior",
                        id: "4b3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                        value: 3567,
                        className: "com.payment.security.BehaviorAnalyzer"
                    }
                ]
            }
        ]
    }
}; 