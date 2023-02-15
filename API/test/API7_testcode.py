#-*- coding: utf-8 -*-
from API_7_TEST.run import inference
import pandas as pd
import ast

# x가 2개인 경우
test_data = [[{'header' : ['appearance','identification'], 'experiment data': 'C:/data/aip/activate/api6/api6_experiment.csv','formulation':[{'excipients':'acacia','kind':'Flavoring agents','max':{'value':'32','unit':'mg'},'use range':{'min':'0','max':'32'},'input range':{'min':'1','max':'12'}},{'excipients':'anhydrous lactose','kind':'filler','max':{'value':'','unit':''},'use range':{'min':'','max':''},'input range':{'min':'2','max':'24'}}],'design-path' : 'C:/data/aip/activate/api7/design/','result-path' : 'C:/data/aip/activate/api7/result/', 'response': [{'Y1': [{'appearance': {'min': 3,'max': 45}}],'Y2': [{'identification': {'min': 3,'max': 54}}]}]}]]
# x가 3개인 경우
# test_data = [[{'header' : ['appearance','identification','dissolution test','assay','impurities'], 'experiment data': 'C:/data/aip/activate/api7/api7_experiment.csv','formulation':[{'excipients':'acacia','kind':'Flavoring agents','max':{'value':'32','unit':'mg'},'use range':{'min':'0','max':'32'},'input range':{'min':'2','max':'3'}},{'excipients':'anhydrous lactose','kind':'filler','max':{'value':'','unit':''},'use range':{'min':'','max':''},'input range':{'min':'5','max':'6'}}, {'excipients':'final','kind':'Flavoring agents','max':{'value':'32','unit':'mg'},'use range':{'min':'0','max':'32'},'input range':{'min':'2','max':'3'}}],'design-path' : 'C:/data/aip/activate/api7/design/','result-path' : 'C:/data/aip/activate/api7/result/', 'response': [{'Y1': [{'appearance': {'min': 3,'max': 422}}],'Y2': [{'identification': {'min': 7.05,'max': 14.00}}],'Y3': [{'dissolution test ': {'min': 0.06,'max': 0.22}}], 'Y4': [{'assay ': {'min': 70,'max': 90}}], 'Y5': [{'impurities': {'min': 0.03,'max': 0.328}}]}]}]]

# parameter
df = pd.DataFrame(test_data)


####################################################################
### effect 범위 -> 실제 소스에는 필요 없음 test하기 편하기 위해서 적은 값###
####################################################################
# value = df.values[0,0]
# experiment = value['experiment data']
# df2 = pd.read_csv(experiment)
# # 각각의 행 데이터를 list로 뽑아서 min, max 추출
# range_list = []
# for i in range(len(value['header'])):
#     # csv의 header y값 : y_value
#     header = value['header']
# #     print('header : ', header)
#     y_value = header[i]
#     print('y_value : ', y_value)
#     # header로 추출한 열 데이터 list로 변환 : convert_y_list
#
#     convert_y_list = df2[y_value].tolist()
#     print('convert_y_list : ', convert_y_list)
#     # 열 데이터에서 min, max 추출
#     min_value = min(convert_y_list)
#     max_value = max(convert_y_list)
#
#     # min, max value type이 int인지 float인지 check 필요, float인 경우 소수점 3째자리에서 반올림 진행해야함
#     if isinstance(min_value, int):
#         pass
#     else:
#         min_value = round(min_value, 3)
#     if isinstance(max_value, int):
#         pass
#     else:
#         max_value = round(max_value, 3)
#     range_dict = {}
#     range_dict["min"] = min_value
#     range_dict["max"] = max_value
#     range_list == [{'min': 2, 'max': 432}, {'min': 7.05, 'max': 14.55}]
#     range_list.append(range_dict)
#     print('range_list : ', range_list)
####################################################################
### end ###
####################################################################

params = ''
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)