from manufacturing.API4_1_FACTOR.run import inference
import pandas as pd

# routes = oral
# test_data = [[{'routes' : 'oral','formulation' : 'Capsule, Oral Capsule','method' : '직타','level' : ['사과', '혼합']}]]

# routes = parenteral
# test_data =  [[{'routes' : 'parenteral','formulation' : 'Injection','method' : '액상제조법','level' : ['사과', '조제(용해)', '무균 여과'],'cqas' : ['성상', '용출시험', '확인시험']}]]

# properties 가 다른 데이터, routes = parenteral, formulation = injection
test_data = [[{'routes' : 'local','formulation' : 'Aerosol','method' : '액상제조법','level' : ['사과', '충전']}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)