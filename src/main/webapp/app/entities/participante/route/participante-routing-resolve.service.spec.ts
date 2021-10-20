jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParticipante, Participante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';

import { ParticipanteRoutingResolveService } from './participante-routing-resolve.service';

describe('Participante routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ParticipanteRoutingResolveService;
  let service: ParticipanteService;
  let resultParticipante: IParticipante | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ParticipanteRoutingResolveService);
    service = TestBed.inject(ParticipanteService);
    resultParticipante = undefined;
  });

  describe('resolve', () => {
    it('should return IParticipante returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticipante).toEqual({ id: 123 });
    });

    it('should return new IParticipante if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipante = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultParticipante).toEqual(new Participante());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Participante })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticipante).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
