window.testData = {
    thread1_1: {
        name: "root",
        value: 57412,
        className: "com.sun.root.MainThread",
        id: "29509a5f-d6bc-47c9-8828-f93f5bc71d6b",
        children: [
            {
                name: "genunix syscall_mstate",
                id: "b25b0d27-ce7b-44e9-b2b6-ff168a6fa36c",
                value: 15789,
                className: "com.sun.genunix.SystemCall",
                children: [
                    {
                        name: "unix memory_allocate",
                        id: "3f5e8d2c-9b7a-45d6-a3c8-1e9f7d6b5a4c",
                        value: 8934,
                        className: "com.sun.unix.memory.MemoryAllocator",
                        children: [
                            {
                                name: "unix page_alloc",
                                id: "2d4c7b6a-8e5f-42d9-b3c8-9e7f6d5c4b3a",
                                value: 4567,
                                className: "com.sun.unix.memory.PageAllocator"
                            },
                            {
                                name: "unix cache_lookup",
                                id: "1c4d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                                value: 3245,
                                className: "com.sun.unix.cache.CacheManager"
                            }
                        ]
                    },
                    {
                        name: "genunix thread_create",
                        id: "5e3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                        value: 5632,
                        className: "com.sun.genunix.thread.ThreadManager",
                        children: [
                            {
                                name: "unix stack_init",
                                id: "4b6a8d2c-7e4f-49b5-a3d8-1c9e7f6d5b4a",
                                value: 2345,
                                className: "com.sun.unix.thread.StackInitializer"
                            }
                        ]
                    }
                ]
            },
            {
                name: "unix process_exec",
                id: "6b4d8c2e-9f5a-34b8-a1c7-8d9e6b5f4c3a",
                value: 12567,
                className: "com.sun.unix.process.ProcessExecutor",
                children: [
                    {
                        name: "unix load_binary",
                        id: "7d2e5f1a-8c3b-45a9-b2d6-9f8e4c7d2b1a",
                        value: 6789,
                        className: "com.sun.unix.loader.BinaryLoader",
                        children: [
                            {
                                name: "unix parse_elf",
                                id: "8a5c9b3d-4f7e-12d5-9e31-7c4521fd9a8e",
                                value: 3456,
                                className: "com.sun.unix.loader.ElfParser"
                            }
                        ]
                    }
                ]
            }
        ]
    },
    thread1_2: {
        name: "root",
        value: 42156,
        className: "com.sun.network.NetworkThread",
        id: "8a5c9b3d-4f7e-12d5-9e31-7c4521fd9a8e",
        children: [
            {
                name: "network socket_read",
                id: "7d2e5f1a-8c3b-45a9-b2d6-9f8e4c7d2b1a",
                value: 18934,
                className: "com.sun.network.SocketOperation",
                children: [
                    {
                        name: "network packet_process",
                        id: "6c4d7b6a-8e5f-42d9-b3c8-9e7f6d5c4b3a",
                        value: 12345,
                        className: "com.sun.network.PacketProcessor",
                        children: [
                            {
                                name: "network decrypt_data",
                                id: "5b3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                                value: 6789,
                                className: "com.sun.network.security.Decryptor"
                            }
                        ]
                    }
                ]
            },
            {
                name: "network socket_write",
                id: "4a7b9c2d-5e3f-41a8-b6d9-2c8f7e4d5b3a",
                value: 15678,
                className: "com.sun.network.SocketOperation",
                children: [
                    {
                        name: "network buffer_flush",
                        id: "3b6a8d2c-7e4f-49b5-a3d8-1c9e7f6d5b4a",
                        value: 8901,
                        className: "com.sun.network.BufferManager"
                    }
                ]
            }
        ]
    },
    thread1_3: {
        name: "root",
        value: 35789,
        className: "com.sun.render.RenderThread",
        id: "6b4d8c2e-9f5a-34b8-a1c7-8d9e6b5f4c3a",
        children: [
            {
                name: "render draw_frame",
                id: "5e3d7c1b-2a9f-48d6-b5e4-3c8f7d9a6b2e",
                value: 20456,
                className: "com.sun.render.FrameRenderer",
                children: [
                    {
                        name: "render compute_layout",
                        id: "4b6a8d2c-7e4f-49b5-a3d8-1c9e7f6d5b4a",
                        value: 12345,
                        className: "com.sun.render.LayoutEngine",
                        children: [
                            {
                                name: "render calculate_bounds",
                                id: "3c5d7e6b-8f5a-42c9-b3d8-9e7f6d5c4b3a",
                                value: 6789,
                                className: "com.sun.render.BoundsCalculator"
                            }
                        ]
                    }
                ]
            }
        ]
    }
}; 