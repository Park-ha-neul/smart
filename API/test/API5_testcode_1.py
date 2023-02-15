#-*- coding: utf-8 -*-
from API_5_TEST.run import inference
import pandas as pd
import ast

test_data = [[{'formulation': [{'excipients': 'acacia','input range': {'min' : 55, 'max' : 100},'kind' : 'z','max' : {'value' : 10, 'unit' : 'mg'},'use range': {'min' : 10, 'max' : 90}},{'excipients': 'anhydrous lactose','input range': {'min' : 2, 'max' : 6},'kind' : 'z','max' : {'value' : 10, 'unit' : 'mg'},'use range': {'min' : 10, 'max' : 90}},{'excipients': 'adipic acid','input range': {'min' : 3, 'max' : 7},'kind' : 'z','max' : {'value' : 10, 'unit' : 'mg'},'use range': {'min' : 10, 'max' : 90}}],'CQAs': ['content uniformity', 'disintegrant test', 'dissolution test']}]]

# parameter
df = pd.DataFrame(test_data)

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)