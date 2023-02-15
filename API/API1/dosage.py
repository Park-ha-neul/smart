def dosage(out_array):
    dosage_form = {}
    o = ['Oral']
    p = ['Parenteral']
    l = ['Local']

    # molecular weight
    mw = out_array[-2]
    if mw < 500:
          dosage_form['Molecular Weight'] = o+p+l
    else :
          dosage_form['Molecular Weight'] = p+l
    
    # Log P
    logP = out_array[6]
    if logP < 1 :
          dosage_form['LogP'] = p
    elif 1 < logP <5 :
          dosage_form['LogP'] = o+p+l
    else: 
          dosage_form['LogP'] = l

    # Log D(PH7)
    logD = out_array[1][6]
    if logD < 1 :
          dosage_form['Log D (pH7)'] = p
    elif 1 < logD <5 :
          dosage_form['Log D (pH7)'] = o+p+l
    else:
          dosage_form['Log D (pH7)'] = l
  
    # pH3 Solubility
    pH3_sol = out_array[0][2]
    if pH3_sol < 0.1 :
          dosage_form['pH3 Solubility'] = o+l
    elif 0.1 < pH3_sol < 1 :
          dosage_form['pH3 Solubility'] = o+p+l
    elif 1 < pH3_sol < 100: 
          dosage_form['pH3 Solubility'] = o+p+l
    else:
          dosage_form['pH3 Solubility'] = p
        
    # pH7 solubility
    pH7_sol= out_array[0][6]
    if pH7_sol < 0.1 :
          dosage_form['pH7 Solubility'] = o+l
    elif 0.1 < pH7_sol < 1 :
          dosage_form['pH7 Solubility'] = o+p+l
    elif 1 < pH7_sol < 100: 
          dosage_form['pH7 Solubility'] = o+p+l
    else:
          dosage_form['pH7 Solubility'] = p

    # permeability (CaCo-2) # dosage from selection rule에선 pear- typo 있음
    import numpy as np
    caco2 = float(out_array[4])
    if caco2 < 10**-6: ## 10-6? 이게 무슨 숫자인가? 10의 마이너스 6승? # 추후 협의해서 알려주겠다. 보통 1.1 이런 식으로 나오는데 저 형식은 잘못된 것 같다.
          dosage_form['Permeability (Caco-2)'] = p
    else:
          dosage_form['Permeability (Caco-2)'] = o+p+l             


    # Bioavailability
    bio = np.round(float(out_array[7]),2)
    if bio < 0.1 :
          dosage_form['Bioavailability'] = p+l
    elif 0.1 < bio < 0.6 : 
          dosage_form['Bioavailability'] = o+p+l
    else:
          dosage_form['Bioavailability'] = o

    return dosage_form


from collections import Counter

def count(out_array):
      # out_array의 값을 가져와서 dosage 함수에 넣어 dosage 딕셔너리를 가져온다.
      dosage_form = dosage(out_array)
      # dosage 딕셔너리의 value만 뽑아내어 2차원의 리스트를 1차원으로 변환한다.
      v = list(dosage_form.values())
      v = sum(v,[])
      # Counter 함수로 각 요소별로 갯수를 세어 딕셔너리로 담는다.
      count = dict(Counter(v))

      return count




'''
"dosage": {
"Molecular weight": ["oral", "parenteral", "local"],      -> out_array[-2]
"Log P": ["oral", "parenteral", "local"],                 -> out_array[6]
"Log D(pH7)": ["parenteral"],                             -> out_array[1][6]
"pH 3 solubility": ["oral", "parenteral", "local"],       -> out_array[0][2]
"pH 7 solubility": ["oral", "parenteral", "local"],       -> out_array[0][6]
"Pearmeability (Caco-2)":["oral", "parenteral", "local"], -> out_array[4]
"Bioavailability": ["oral"]                               -> out_array[7]

  "count": {
          "oral" : 5,
          "parenteral" : 3,
          "local" : 2
      }

'''