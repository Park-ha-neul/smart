from manufacturing.API1_method.run import inference
import pandas as pd

# routes = oral, formulation = Capsule
# test_data =  [[{'properties': {'pH Mass Solubility': [-44.72, 6.57, 33.54, -2.5, 48.63, 14.22, 17.91, 13.4, 46.8, 60.89], 'pH logD': [-1.12, -0.36, -0.85, -0.66, -0.59, -1.24, -0.96, -0.24, -0.67, -0.5], 'pKa': 17.97, 'pKb': 0.03, 'Caco2 Permeability': ['Permeable with Papp > 8 * 10^-6 (cm/s)', 1.0], 'Boiling point(°C)': 340.98, 'logP': -7.95, 'Bioavailability': ['Orally bioavailable', 1.0], 'Dosage Form': ['Dosage Form: Non-oral', 1.0], 'Molecular weight(g/mol)': 194.19}, 'routes': 'oral', 'formulation': 'Capsule, Oral Capsule', 'primary': 12}]]

# properties 가 다른 데이터, routes = parenteral, formulation = injection
# test_data = [[{'properties': {'pH Mass Solubility': [-222.83,-28.96,340.45,480.17,-383.73,-397.63,358.04,480.22,458.53,-163.85], 'pH logD': [1.4,-0.27,2.43,10.56,4.14,5.84,5.49,0.48,2.07,-0.51], 'pKa': 5.81, 'pKb': 0.84, 'Caco2 Permeability': ["Non-permeable with Papp > 8 * 10^-6 (cm/s)","0.83"], 'Boiling point(°C)': 552.42, 'logP': 0.25, 'Bioavailability': ["Orally bioavailable","0.67"], 'Dosage Form': ['Dosage Form: Non-oral', 1.0], 'Molecular weight(g/mol)': 194.19}, 'routes': 'parenteral', 'formulation': 'Injection', 'primary': 12}]]

# routes = local, formulation = Opthalmic gel
test_data = [[{'properties': {'pH Mass Solubility': [-222.83,-28.96,340.45,480.17,-383.73,-397.63,358.04,480.22,458.53,-163.85], 'pH logD': [1.4,-0.27,2.43,10.56,4.14,5.84,5.49,0.48,2.07,-0.51], 'pKa': 5.81, 'pKb': 0.84, 'Caco2 Permeability': ["Non-permeable with Papp > 8 * 10^-6 (cm/s)","0.83"], 'Boiling point(°C)': 552.42, 'logP': 0.25, 'Bioavailability': ["Orally bioavailable","0.67"], 'Dosage Form': ['Dosage Form: Non-oral', 1.0], 'Molecular weight(g/mol)': 194.19}, 'routes': 'local', 'formulation': 'Aerosol', 'primary': 12}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)