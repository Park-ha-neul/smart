from manufacturing.API4_FMEA.run import inference
import pandas as pd

# routes = oral
# test_data = [[{'routes' : 'oral','formulation' : 'Tablet','method' : '직타','level' : ['사과', '혼합'], 'path' : '/data/aip/activate/manufacturing/api4/img2/'}]]
## 피드백 반영 후 바뀐 input data
# test_data = [[{'routes' : 'oral','formulation' : 'Tablet','method' : '습식과립','level' : ['사과', '혼합', '과립'], 'path' : '/data/aip/activate/manufacturing/api4/img2/', 'pha' : [{'사과' : ['낮음', '낮음', '낮음', '낮음', '낮음']}, {'혼합' : ['중간', '중간', '중간', '중간', '낮음']}, {'과립' : ['낮음', '낮음', '낮음', '낮음', '낮음']}]}]]

# routes = parenteral
# test_data = [[{'routes' : 'parenteral','formulation' : 'Injection','method' : '액상제조법','level' : ['사과', '조제(용해)', '무균 여과'], 'path' : '/data/aip/activate/manufacturing/api4/img2/'}]]
## 피드백 반영 후 바뀐 input data
# test_data = [[{'routes' : 'parenteral','formulation' : 'Injection','method' : '동결건조제조법','level' : ['사과', '조제(용해)', '무균 여과'], 'path' : '/data/aip/activate/manufacturing/api4/img2/', 'pha' : [{'사과' : ['높음', '낮음', '낮음', '낮음', '낮음']}, {'조제(용해)' : ['높음', '중간', '중간', '중간', '낮음']}, {'무균 여과' : ['낮음', '낮음', '낮음', '낮음', '낮음']}]}]]

# properties 가 다른 데이터, routes = parenteral, formulation = injection
# test_data = [[{'routes' : 'local','formulation' : 'Ophthalmic Solution','method' : '에멀전제조법','level' : ['사과', '무균 여과'],'path' : '/data/aip/activate/manufacturing/api4/img2/'}]]
## 피드백 반영 후 바뀐 input data
test_data = [[{'routes' : 'local','formulation' : 'Ophthalmic Solution','method' : '에멀전제조법','level' : ['사과', '무균 여과'], 'path' : '/data/aip/activate/manufacturing/api4/img2/', 'pha' : [{'사과' : ['높음', '낮음', '낮음', '낮음', '낮음']}, {'무균 여과' : ['낮음', '중간', '중간', '중간', '낮음']}]}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)