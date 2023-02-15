from manufacturing.API2_list.run import inference
import pandas as pd

# routes = oral
# test_data = [[{'routes' : 'oral','formulation' : 'Capsule, Oral Capsule','method': '직타'}]]

# routes = parenteral
# test_data =  [[{'routes' : 'parenteral','formulation' : 'Injection','method': '액상제조법'}]]

# properties 가 다른 데이터, routes = parenteral, formulation = injection
test_data = [[{'routes' : 'local','formulation' : 'Ophthalmic Solution','method': '에멀전제조법'}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)