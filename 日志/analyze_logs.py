import json

with open('dplogs2.json', 'r') as f:
    data = json.load(f)

# 按时间戳排序
data_sorted = sorted(data, key=lambda x: x.get('timestamp', ''))

print(f'总日志条数: {len(data_sorted)}')
print(f'时间范围: {data_sorted[0]["timestamp"]} ~ {data_sorted[-1]["timestamp"]}')
print()

print('=' * 80)
print('=== 最早50条消息（应用启动序列） ===')
print('=' * 80)
for i, entry in enumerate(data_sorted[:50]):
    msg = entry.get('message', '')[:300]
    ts = entry.get('timestamp', '')
    lvl = entry.get('attributes', {}).get('level', entry.get('severity', ''))
    print(f'[{i:4d}] [{lvl:5s}] {ts[11:19]}: {msg}')

print()
print('=' * 80)
print('=== 搜索关键模式 ===')
print('=' * 80)

keywords = [
    'Exception', 'Error', 'error', 'Failed', 'failed', 
    'Bean', 'bean', 'Mapped', 'mapped',
    'Controller', 'controller', 'Service', 'service',
    'Category', 'User', 'Diagnostic', 'diag',
    'Started', 'started', 'Running', 'running',
    'lazy-initialization', 'circular', 'exclude',
    'ResourceHandler', 'addResourceHandlers',
    'No static resource', '403', '500', 'Forbidden',
    'auto-config', 'AutoConfiguration',
    'Tomcat started', 'port', 'Profile'
]

for kw in keywords:
    matches = [e for e in data_sorted if kw.lower() in e.get('message', '').lower()]
    if matches:
        print(f'\n--- [{kw}] ({len(matches)} matches) ---')
        for m in matches[:10]:
            msg = m.get('message', '')[:250]
            ts = m['timestamp'][11:19]
            print(f'  [{ts}] {msg}')
