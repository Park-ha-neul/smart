from API2.run import inference
import pandas as pd

#input data
# CASE1) 'routes' : 'oral'
# CASE2) 'routes' : 'parenteral'
# CASE3) 'routes' : 'local'
test_data =  [[{'routes':'local'}]]

# parameter
df = pd.DataFrame(test_data)
params = {"routes": [["routes_classification", "include"]], "oral": [["Capsule, Oral Capsule", "Capsule, Oral Capsule"], ["Tablet", "Tablet "], ["Oral Suspension, Suspension", "Oral Suspension, Suspension"], ["Oral Solution", "Oral solution, Elixir, Drop, Liquid, Syrup"], ["Granule", "Granule, Gum, Troche"], ["Powder", "Powder"], ["Gel", "Gel"], ["Sublingual", "Sublingual Tablet Subblingual Film, \nSubblingual Powder"], ["Sublingual Spray", "Subblingual Spray"], ["Bucal Tablet, Bucal Film", "Bucal Tablet & Bucal Film, Film"]], "local": [["Aerosol", "Injection"], ["Transmucosal Lozenge", "Intramuscular injection"], ["Topical Suspension, Topical Solution", "Intravenous Injection"], ["Cream, Topical Cream", "Implant, Subcutaneous Implant, \nSubcutaneous injection"], ["Emusion", "Injection Suspension"], ["Gel, Topical Gel", "Aerosol, Powder"], ["Lotion", "Transmucosal Lozenge"], ["Ointment", "Topical Suspension, Topical Solution"], ["Patch", "Cream, Topical Cream"], ["Shampoo, Topical Shampoo", "Emusion"], ["Nasal Spray, Nasal Solution", "Gel, Paste"], ["Spray", "Lotion"], ["Ophthalmic gel", "Ointment"], ["Ophthalmic solution", "Patch"], ["Ophthalmic suspension, Ophthalmic Emulsion", "Shampoo, Topical Shampoo"], ["INTRAVITREAL IMPLANT", "Nasal Spray, Nasal Solution"], ["Suppository", "Spray"], ["Suspension", "Ophthalmic gel"], ["Vaginal ", "Ophthalmic solution"], ["URETHRAL SUPPOSITORY", "Ophthalmic suspension, Ophthalmic Emulsion"]], "parenteral": [["Injection", "INTRAVITREAL IMPLANT"], ["Intramuscular injection", "Suppository"], ["Intravenous Injection", "Suspension"], ["Implant", "Vaginal Ring, Vaginal insert, \nVaginal gel, Vaginal Cream"], ["Injection Suspension", "URETHRAL SUPPOSITORY"]]}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)