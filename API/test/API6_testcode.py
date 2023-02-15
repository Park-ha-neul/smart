#-*- coding: utf-8 -*-
from API_6_TEST.run import inference
import pandas as pd
import ast


# x가 2개인 경우
# test_data = [[{'header' : ['content uniformity','dissolution test','appearance','identification','assay', 'impurities'], 'experiment data': 'C:/data/aip/activate/api6/api6_experiment.csv','formulation':[{'excipients':'acacia','kind':'Flavoring agents','max':{'value':'32','unit':'mg'},'use range':{'min':'0','max':'32'},'input range':{'min':'2','max':'3'}},{'excipients':'anhydrous lactose','kind':'filler','max':{'value':'','unit':''},'use range':{'min':'','max':''},'input range':{'min':'5','max':'6'}}],'pareto-path' : 'C:/data/aip/activate/api6/pareto/','contour-path' : 'C:/data/aip/activate/api6/contour/','response-path' : 'C:/data/aip/activate/api6/response/'}]]

# x가 3개인 경우
test_data = [[{
    'header' : ['appearance', 'identification'],
    'experiment data': 'C:/data/aip/activate/api6/api6_experiment.csv',
    'formulation':[
        {'excipients': 'acacia','input range': {'min' : 1, 'max' : 12},'kind' : 'z','max' : {'value' : 12, 'unit' : 'mg'},'use range': {'min' : 0, 'max' : 32}},
        {'excipients': 'anhydrous lactose','input range': {'min' : 2, 'max' : 24},'kind' : 'z','max' : {'value' : 10, 'unit' : 'mg'},'use range': {'min' : 10, 'max' : 90}}],
    'pareto-path' : 'C:/data/aip/activate/api6/pareto/',
    'contour-path' : 'C:/data/aip/activate/api6/contour/',
    'response-path' : 'C:/data/aip/activate/api6/response/'}]]
# parameter
df = pd.DataFrame(test_data)

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)