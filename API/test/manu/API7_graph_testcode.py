#-*- coding: utf-8 -*-
from manufacturing.API7_GRAPH.run import inference
import pandas as pd
import ast


# x가 2개인 경우
test_data = [[{'header' : ['appearance', 'identification'], 'experiment data': 'C:/data/aip/activate/manufacturing/api7/api8_experiment.csv','cpp': [{'factor': 'sifting','input range': {'min' : 1, 'max' : 12}},{'factor': 'mill type','input range': {'min' : 2, 'max' : 24}}],'pareto-path' : 'C:/data/aip/activate/manufacturing/api7/pareto/','contour-path' : 'C:/data/aip/activate/manufacturing/api7/contour/','response-path' : 'C:/data/aip/activate/manufacturing/api7/response/'}]]

# x가 3개인 경우
# test_data = [[{'header' : ['appearance', 'identification', 'dissolution test', 'assay', 'impurities'], 'experiment data': 'C:/data/aip/activate/manufacturing/api7/api7_experiment.csv','cpp': [{'factor': 'sifting','input range': {'min' : 55, 'max' : 100}},{'factor': 'mill type','input range': {'min' : 2, 'max' : 6}},{'factor': 'mill speed','input range': {'min' : 3, 'max' : 7}}],'pareto-path' : 'C:/data/aip/activate/manufacturing/api7/pareto/','contour-path' : 'C:/data/aip/activate/manufacturing/api7/contour/','response-path' : 'C:/data/aip/activate/manufacturing/api7/response/'}]]
# parameter
df = pd.DataFrame(test_data)

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)