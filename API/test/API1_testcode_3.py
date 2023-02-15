from API1.origin import inference
import pandas as pd

#input data
# input type = "sdf"
test_data =  [[{'reqid' : 'dd','type':'sdf', 'value':'/data/aip/api/sdf/Bromhexine.sdf', 'path':'/data/test/'}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)