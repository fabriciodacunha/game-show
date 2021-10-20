jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPlay, Play } from '../play.model';
import { PlayService } from '../service/play.service';

import { PlayRoutingResolveService } from './play-routing-resolve.service';

describe('Play routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PlayRoutingResolveService;
  let service: PlayService;
  let resultPlay: IPlay | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PlayRoutingResolveService);
    service = TestBed.inject(PlayService);
    resultPlay = undefined;
  });

  describe('resolve', () => {
    it('should return IPlay returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlay = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlay).toEqual({ id: 123 });
    });

    it('should return new IPlay if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlay = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPlay).toEqual(new Play());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Play })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlay = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlay).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
