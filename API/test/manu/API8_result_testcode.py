#-*- coding: utf-8 -*-
from manufacturing.API8_RESULT.run import inference
import pandas as pd
import ast


# x가 2개인 경우
test_data = [[{'header' : ['appearance', 'identification'], 'experiment data': 'C:/data/aip/activate/manufacturing/api7/api8_experiment.csv','cpp': [{'factor': 'sifting','input range': {'min' : 1, 'max' : 12}},{'factor': 'mill type','input range': {'min' : 2, 'max' : 24}}],'design-path' : 'C:/data/aip/activate/manufacturing/api8/design/','result-path' : 'C:/data/aip/activate/manufacturing/api8/result/', 'response': [{'Y1': [{'appearance': {'min': 3,'max': 45}}],'Y2': [{'identification': {'min': 3,'max': 54}}]}]}]]

# x가 3개인 경우
# test_data = [[{'header' : ['appearance', 'identification', 'dissolution test', 'assay', 'impurities'], 'experiment data': 'C:/data/aip/activate/manufacturing/api7/api7_experiment.csv','cpp': [{'factor': 'sifting','input range': {'min' : 55, 'max' : 100}},{'factor': 'mill type','input range': {'min' : 2, 'max' : 6}},{'factor': 'mill speed','input range': {'min' : 3, 'max' : 7}}],'design-path' : 'C:/data/aip/activate/manufacturing/api8/design/','result-path' : 'C:/data/aip/activate/manufacturing/api8/result/', 'response': [{'Y1': [{'appearance': {'min': 3,'max': 422}}],'Y2': [{'identification': {'min': 7.05,'max': 14.00}}],'Y3': [{'dissolution test ': {'min': 0.06,'max': 0.22}}], 'Y4': [{'assay ': {'min': 70,'max': 90}}], 'Y5': [{'impurities': {'min': 0.03,'max': 0.328}}]}]}]]

# parameter
df = pd.DataFrame(test_data)

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)