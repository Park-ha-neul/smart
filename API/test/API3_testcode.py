from API3.run2 import inference, init_svc
import pandas as pd
import ast

#input data
# API3 설명.
# step1. 입력 받은 smiles에 따라서 그에 해당하는 ingredient(재료) 추출
# step2. 그 ingredient에 해당하는 kind(종류) 추출
# step3. ingredient와 입력받은 formulation(제형)에 해당하는 use_range(사용가능한 용량) 추출
# step4. 추출한 use_range에서 최대값을 max(최대치용량)으로 추출
test_data =  [[{'smiles': 'CN1C=NC2=C1C(=O)N(C(=O)N2C)C','formulation' : 'Tablet','primary': {'value': 12,'unit': 'ml'}}]]
# parameter
df = pd.DataFrame(test_data)
batch_id = 1

# call inference
params = init_svc('')
res = inference(df, params, batch_id)