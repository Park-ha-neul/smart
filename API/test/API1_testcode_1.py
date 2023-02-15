from API1.origin import inference
import pandas as pd

#input data
# input type = "chemical"
test_data =  [[{'reqid' : 'dd','type':'chemical', 'value':'CLOBETASOL PROPIONATE', 'path':'/data/test/'}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)