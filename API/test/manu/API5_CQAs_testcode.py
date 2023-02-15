from manufacturing.API5_CQAs.run import inference
import pandas as pd

# routes = oral
test_data = [[{'route' : 'oral','formulation' : 'Capsule, Oral Capsule','method' : '습식과립'}]]

# routes = parenteral
# test_data = [[{'route' : 'parenteral','formulation' : 'Injection','method' : '액상제조법'}]]

# routes = local
# test_data = [[{'route' : 'local','formulation' : 'Aerosol','method' : '에멀전제조법'}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)