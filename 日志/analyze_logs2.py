import json

with open('dplogs2.json', 'r') as f:
    data = json.load(f)

data_sorted = sorted(data, key=lambda x: x.get('timestamp', ''))

print('=' * 80)
print('=== 完整ERROR消息（GlobalExceptionHandler） ===')
print('=' * 80)
for i, entry in enumerate(data_sorted):
    msg = entry.get('message', '')
    if 'GlobalExceptionHandler' in msg or 'ERROR' in msg or 'No static resource' in msg:
        ts = entry['timestamp'][11:19]
        print(f'\n[{ts}] {msg[:400]}')

print()
print('=' * 80)
print('=== 所有非堆栈跟踪的消息（去除\tat开头的行） ===')
print('=' * 80)
count = 0
for i, entry in enumerate(data_sorted):
    msg = entry.get('message', '').strip()
    ts = entry['timestamp'][11:19]
    # 跳过堆栈跟踪行
    if msg.startswith('\tat ') or msg.startswith('   at '):
        continue
    if msg == '':
        continue
    count += 1
    print(f'[{count:3d}] [{ts}] {msg[:350]}')
    if count > 80:
        print(f'... (还有更多)')
        break

print()
print(f'\n总有效消息数: {count}')
