from API4.run import inference
import pandas as pd
import ast
import json

# input data
test_data =  [[{'formulation': 'Injection'}]]

# 조건에 맞지 않는 input data
# test_data =  [[{'formulation': 'Topical Suspension, Topical Solution'}]]

# parameter
df = pd.DataFrame(test_data)

## 데이터 넣는것 까지 확인
with open('/data/aip/activate/CQAs_list.json') as json_file:
    json_data = json.load(json_file)
params = json_data
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)