#-*- coding: utf-8 -*-
from  manufacturing.API6_DOE.run import inference
import pandas as pd
import ast

# test_data = [[{'cpp': [{'factor': 'sifting','input range': {'min' : 55, 'max' : 100}},{'factor': 'mill type','input range': {'min' : 2, 'max' : 6}},{'factor': 'mill speed','input range': {'min' : 3, 'max' : 7}}],'CQAs': ['content uniformity', 'disintegrant test', 'dissolution test']}]]

test_data = [[{'cpp': [{'factor': 'Binder-granulating agent spraying rate','input range': {'min' : 10, 'max' : 40}},{'factor': 'Mixing speed','input range': {'min' : 20, 'max' : 30}},{'factor': 'Sifting','input range': {'min' : 1, 'max' : 10}}],'CQAs': ['content uniformity', 'disintegrant test', 'dissolution test']}]]

# parameter
df = pd.DataFrame(test_data)

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)