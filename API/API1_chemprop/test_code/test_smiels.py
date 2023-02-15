from Run_chemprop import inference

import pandas as pd

#input data
# input type = "smiles"
test_data =  [[{'reqid' : 'dd','type':'smiles', 'value':'CN(CC1=C(C(=CC(=C1)Br)Br)N)C2CCCCC2', 'path':'/data/test/'}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)
