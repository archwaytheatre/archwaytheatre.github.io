const photoData = [
['2024/much-ado/photo-0017.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0008.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/pan/photo-0019.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0015.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0004.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0002.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/much-ado/photo-0020.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/much-ado/photo-0007.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0007.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/constellations/photo-0011.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0018.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/much-ado/photo-0003.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0004.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0005.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0011.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0021.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0010.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/much-ado/photo-0019.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0021.png', 'Hannah Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0001.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0018.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0015.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0017.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0013.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0013.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0001.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0001.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0002.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0005.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0006.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/constellations/photo-0012.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0015.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0016.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0006.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0010.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/pan/photo-0024.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0022.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0016.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/constellations/photo-0009.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/pan/photo-0012.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0018.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0014.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/constellations/photo-0020.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0014.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0007.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/constellations/photo-0002.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/pan/photo-0008.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/constellations/photo-0014.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0004.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0019.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0012.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0010.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0017.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0006.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0003.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/much-ado/photo-0016.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/much-ado/photo-0009.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/constellations/photo-0013.png', 'Eddie Redfern', '2024', 'Constellations'],
  ['2024/pan/photo-0009.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/much-ado/photo-0008.png', 'Noelle Vaughn', '2024', 'Much Ado About Nothing'],
  ['2024/pan/photo-0005.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0025.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0003.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0023.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0011.png', 'Eddie Redfern', '2024', 'Peter Pan'],
  ['2024/pan/photo-0020.png', 'Eddie Redfern', '2024', 'Peter Pan']
];